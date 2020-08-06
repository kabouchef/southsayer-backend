package fr.personnel.southsayerbackend.service.activitycode;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerbackend.service.simulation.core.StaticPathService;
import fr.personnel.southsayerbackend.utils.ExcelUtils;
import fr.personnel.southsayerdatabase.entity.activitycode.ActivityCode;
import fr.personnel.southsayerdatabase.repository.activitycode.ActivityCodeRepository;
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
 * Activity Code Service
 * @version 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityCodeService {

    private final ActivityCodeRepository activityCodeRepository;
    private final NotFoundMessage notFoundMessage;
    private final StaticPathService staticPathService;

    String fileName = "LM - " + this.getClass().getSimpleName().replace("Service","");

    /**
     * Get Path to export file
     * @return {@link String}
     */
    private String getPath(){
        return this.staticPathService.getPath(XLS_EXTENSION, STATIC_DIRECTORY_ACTIVITY_CODE);
    }

    /**
     * Get all AC
     *
     * @return {@link Iterable<ActivityCode>}
     */
    public Iterable<ActivityCode> getAll() throws IOException {

        Iterable<ActivityCode> activityCodes = this.activityCodeRepository.findAll();

        ExcelUtils.writeToExcel((List<ActivityCode>) activityCodes, fileName, this.getPath());

        return activityCodes;
    }

    /**
     * Get all AC by code activite
     *
     * @param codActivite : codActivite
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByCodActivite(String codActivite) throws IOException {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByCodActivite(codActivite);

        if (!activityCodes.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(codActivite));

        ExcelUtils.writeToExcel(activityCodes.get(), fileName, this.getPath());

        return activityCodes.get();
    }

    /**
     * Get all AC by lib1
     *
     * @param lib1 : lib1
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByLib1(String lib1) throws IOException {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByLib1Like(lib1);

        if (!activityCodes.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(lib1));

        ExcelUtils.writeToExcel(activityCodes.get(), fileName, this.getPath());

        return activityCodes.get();
    }

    /**
     * Get all AC by id rayon
     *
     * @param idRayon : idRayon
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByIdRayon(Long idRayon) throws IOException {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByIdRayon(idRayon);

        if (!activityCodes.isPresent()) throw new NotFoundException(this.notFoundMessage.toLong(idRayon));

        ExcelUtils.writeToExcel(activityCodes.get(), fileName, this.getPath());

        return activityCodes.get();
    }

    /**
     * Get all AC by rayon
     *
     * @param rayon : rayon
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByRayon(String rayon) throws IOException {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByRayon(rayon);

        if (!activityCodes.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(rayon));

        ExcelUtils.writeToExcel(activityCodes.get(), fileName, this.getPath());

        return activityCodes.get();
    }

    /**
     * Get all AC by rayon
     *
     * @param idOAP : idOAP
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByIdOAP(String idOAP) throws IOException {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByIdOap(idOAP);

        if (!activityCodes.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(idOAP));

        ExcelUtils.writeToExcel(activityCodes.get(), fileName, this.getPath());

        return activityCodes.get();
    }


    /**
     * Save OAP AC
     *
     * @param activityCodes : OAP Activity codes
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> save(List<ActivityCode> activityCodes) {

        return activityCodes.stream().map(this.activityCodeRepository::save).collect(Collectors.toList());
    }

    /**
     * Delete OAP AC by Cod_Activite
     *
     * @param listCodActivite : List Cod_Activite
     */
    public void deleteByCodActivite(List<String> listCodActivite) {
        listCodActivite.forEach(this.activityCodeRepository::deleteByCodActivite);
    }

}
