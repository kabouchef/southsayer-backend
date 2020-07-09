package fr.personnel.southsayerbackend.service.activityCode;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerdatabase.entity.activityCode.ActivityCode;
import fr.personnel.southsayerdatabase.repository.activityCode.ActivityCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Activity Code Service
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityCodeService {

    private final ActivityCodeRepository activityCodeRepository;

    /**
     * Get all AC
     *
     * @return {@link List<ActivityCode>}
     */
    public Iterable<ActivityCode> getAll() {
        return this.activityCodeRepository.findAll();
    }

    /**
     * Get all AC by code activite
     *
     * @param codActivite : codActivite
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByCodActivite(String codActivite) {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByCodActivite(codActivite);

        if (!activityCodes.isPresent()) throw new NotFoundException("\"" + codActivite + "\" was not found.");
        return activityCodes.get();
    }

    /**
     * Get all AC by lib1
     *
     * @param lib1 : lib1
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByLib1(String lib1) {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByLib1Like(lib1);

        if (!activityCodes.isPresent()) throw new NotFoundException("\"" + lib1 + "\" was not found.");
        return activityCodes.get();
    }

    /**
     * Get all AC by id rayon
     *
     * @param idRayon : idRayon
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByIdRayon(Long idRayon) {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByIdRayon(idRayon);

        if (!activityCodes.isPresent()) throw new NotFoundException("\"" + idRayon + "\" was not found.");
        return activityCodes.get();
    }

    /**
     * Get all AC by rayon
     *
     * @param rayon : rayon
     * @return {@link List<ActivityCode>}
     */
    public List<ActivityCode> getByRayon(String rayon) {
        Optional<List<ActivityCode>> activityCodes = this.activityCodeRepository.findByRayon(rayon);

        if (!activityCodes.isPresent()) throw new NotFoundException("\"" + rayon + "\" was not found.");
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
