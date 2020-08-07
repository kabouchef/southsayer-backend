package fr.personnel.southsayerbackend.service.rate;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerbackend.service.simulation.core.StaticPathService;
import fr.personnel.southsayerbackend.utils.ExcelUtils;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryRateDetails;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryType;
import fr.personnel.southsayerdatabase.repository.rate.OAPDeliveryTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.*;

/**
 * @author Farouk KABOUCHE
 * OAP Type Rate Service
 * @version 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OAPTypeRateService {

    private final OAPDeliveryTypeRepository oapDeliveryTypeRepository;
    private final NotFoundMessage notFoundMessage;
    private final StaticPathService staticPathService;

    String fileName = "LM - " + this.getClass().getSimpleName().replace("Service","");

    /**
     * Get Path to export file
     * @return {@link String}
     */
    private String getPath(){
        return this.staticPathService.getPath(XLS_EXTENSION, STATIC_DIRECTORY_TYPE_RATE);
    }

    /**
     * Get all OAP DT
     *
     * @return {@link Iterable<OAPDeliveryType>}
     */
    public Iterable<OAPDeliveryType> getAll() throws IOException {

        Iterable<OAPDeliveryType> oapDeliveryTypes = this.oapDeliveryTypeRepository.findAll();

        ExcelUtils.writeToExcel((List<OAPDeliveryType>) oapDeliveryTypes, fileName, this.getPath(), "all");

        return oapDeliveryTypes;
    }

    /**
     * Get all OAP DT by Code type prestation
     *
     * @param codTypePrestation : Code Type Prestation
     * @return {@link List<OAPDeliveryType>}
     */
    public List<OAPDeliveryType> getByCodTypePrestation(String codTypePrestation) throws IOException {
        Optional<List<OAPDeliveryType>> oapDeliveryTypes =
                this.oapDeliveryTypeRepository.findByCodTypePrestationLike(codTypePrestation);

        if (!oapDeliveryTypes.isPresent())
            throw new NotFoundException(this.notFoundMessage.toString(codTypePrestation));

        ExcelUtils.writeToExcel(oapDeliveryTypes.get(), fileName, this.getPath(), codTypePrestation);
        return oapDeliveryTypes.get();
    }

    /**
     * Get all OAP DT by Lib Type Prestation
     *
     * @param wording : wording
     * @return {@link List<OAPDeliveryType>}
     */
    public List<OAPDeliveryType> getByWordingDT(String wording) throws IOException {
        Optional<List<OAPDeliveryType>> oapDeliveryTypes =
                this.oapDeliveryTypeRepository.findByLibTypePrestationLike(wording);
        if (!oapDeliveryTypes.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(wording));
        ExcelUtils.writeToExcel(oapDeliveryTypes.get(), fileName, this.getPath(), wording);
        return oapDeliveryTypes.get();
    }

    /**
     * Get all OAP DT by idOAP
     *
     * @param idOAP : idOAP
     * @return {@link List<OAPDeliveryType>}
     */
    public List<OAPDeliveryType> getByIdOAP(Long idOAP) throws IOException {
        Optional<List<OAPDeliveryType>> oapDeliveryTypes = this.oapDeliveryTypeRepository.findByIdOap(idOAP);
        if (!oapDeliveryTypes.isPresent()) throw new NotFoundException(this.notFoundMessage.toLong(idOAP));

        ExcelUtils.writeToExcel(oapDeliveryTypes.get(), fileName, this.getPath(), idOAP.toString());

        return oapDeliveryTypes.get();
    }


    /**
     * Save OAP DT
     *
     * @param oapDeliveryTypes : All OAP DT
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryType> save(List<OAPDeliveryType> oapDeliveryTypes) {

        return oapDeliveryTypes.stream().map(this.oapDeliveryTypeRepository::save).collect(Collectors.toList());
    }

    /**
     * Delete OAP DT
     *
     * @param listCodTypePrestation : list Codes Type Prestation
     */
    public void deleteByCodTypePrestation(List<String> listCodTypePrestation) {
        listCodTypePrestation.forEach(this.oapDeliveryTypeRepository::deleteByCodTypePrestation);
    }
}
