package fr.personnel.southsayerbackend.service.rate;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryRateDetails;
import fr.personnel.southsayerdatabase.repository.rate.OAPDeliveryRateDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Farouk KABOUCHE
 *
 * OAP Delivery Rate Service
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OAPDeliveryRateService {

    private final OAPDeliveryRateDetailsRepository oapDeliveryRateDetailsRepository;


    /**
     * get all delivery rates by id
     *
     * @param id : id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryRateDetails> getByIdentifiant(String id) {
        Optional<List<OAPDeliveryRateDetails>> oapDeliveryRateDetails = this.oapDeliveryRateDetailsRepository.findByIdentifiantLike(id);

        if (!oapDeliveryRateDetails.isPresent())
            throw new NotFoundException("\"" + id + "\"" + " with the following id : " + id + " was not found.");
        return oapDeliveryRateDetails.get();
    }

    /**
     * Get all delivery rates by wording
     *
     * @param id : id
     * @param libelleId : libelleId
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryRateDetails> getByLibelleId(String id, String libelleId) {
        Optional<List<OAPDeliveryRateDetails>> oapDeliveryRateDetails = this.oapDeliveryRateDetailsRepository.findByIdentifiantLikeAndLibelleIdentifiantLike(id, libelleId);

        if (!oapDeliveryRateDetails.isPresent())
            throw new NotFoundException("\"" + libelleId + "\"" + " with the following id : " + id + " was not found.");
        return oapDeliveryRateDetails.get();
    }

    /**
     * Get all delivery rates by designation detail
     *
     * @param id : id
     * @param designation : designation
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryRateDetails> getByDesignation(String id, String designation) {

        Optional<List<OAPDeliveryRateDetails>> oapDeliveryRateDetails = this.oapDeliveryRateDetailsRepository.findByIdentifiantLikeAndDesignationLike(id, designation);

        if (!oapDeliveryRateDetails.isPresent())
            throw new NotFoundException("\"" + designation + "\"" + " with the following id : " + id + " was not found.");
        return oapDeliveryRateDetails.get();
    }

    /**
     * Save OAP DR
     *
     * @param oapDeliveryRateDetails : OAP DR Details
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryRateDetails> save(List<OAPDeliveryRateDetails> oapDeliveryRateDetails) {

        return oapDeliveryRateDetails.stream()
                .map(this.oapDeliveryRateDetailsRepository::save)
                .collect(Collectors.toList());
    }

    /**
     * Delete OAP DR by Id
     *
     * @param listId : List id
     */
    public void deleteByIdentifiant(List<String> listId) {
        listId.forEach(this.oapDeliveryRateDetailsRepository::deleteByIdentifiant);
    }
}
