package fr.personnel.southsayerbackend.service.rate;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryRateDetails;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryType;
import fr.personnel.southsayerdatabase.repository.rate.OAPDeliveryTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Get all OAP DT
     *
     * @return {@link Iterable<OAPDeliveryType>}
     */
    public Iterable<OAPDeliveryType> getAll() {
        return this.oapDeliveryTypeRepository.findAll();
    }

    /**
     * Get all OAP DT by Code type prestation
     *
     * @param codTypePrestation : Code Type Prestation
     * @return {@link List<OAPDeliveryType>}
     */
    public List<OAPDeliveryType> getByCodTypePrestation(String codTypePrestation) {
        Optional<List<OAPDeliveryType>> oapDeliveryTypes =
                this.oapDeliveryTypeRepository.findByCodTypePrestationLike(codTypePrestation);

        if (!oapDeliveryTypes.isPresent())
            throw new NotFoundException(this.notFoundMessage.toString(codTypePrestation));
        return oapDeliveryTypes.get();
    }

    /**
     * Get all OAP DT by Lib Type Prestation
     *
     * @param wording : wording
     * @return {@link List<OAPDeliveryType>}
     */
    public List<OAPDeliveryType> getByWordingDT(String wording) {
        Optional<List<OAPDeliveryType>> oapDeliveryTypes = this.oapDeliveryTypeRepository.findByLibTypePrestationLike(wording);

        if (!oapDeliveryTypes.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(wording));
        return oapDeliveryTypes.get();
    }

    /**
     * Get all OAP DT by idOAP
     *
     * @param idOAP : idOAP
     * @return {@link List<OAPDeliveryType>}
     */
    public List<OAPDeliveryType> getByIdOAP(Long idOAP) {
        Optional<List<OAPDeliveryType>> oapDeliveryTypes = this.oapDeliveryTypeRepository.findByIdOap(idOAP);

        if (!oapDeliveryTypes.isPresent()) throw new NotFoundException(this.notFoundMessage.toLong(idOAP));

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
