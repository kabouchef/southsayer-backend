package fr.personnel.southsayerbackend.service.rate;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerbackend.service.simulation.core.StaticPathService;
import fr.personnel.southsayerbackend.utils.ExcelUtils;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryRateDetails;
import fr.personnel.southsayerdatabase.repository.rate.OAPDeliveryRateDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.*;

/**
 * @author Farouk KABOUCHE
 * OAP Delivery Rate Service
 * @version 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OAPDeliveryRateService {

    private final OAPDeliveryRateDetailsRepository oapDeliveryRateDetailsRepository;
    private final NotFoundMessage notFoundMessage;
    private final StaticPathService staticPathService;

    String fileName = "LM - " + this.getClass().getSimpleName().replace("Service","");

    /**
     * Get Path to export file
     * @return {@link String}
     */
    private String getPath(){
        return this.staticPathService.getPath(XLS_EXTENSION, STATIC_DIRECTORY_DELIVERY_RATE);
    }

    /**
     * get all DR
     *
     * @return {@link Iterable<OAPDeliveryRateDetails>}
     */
    public Iterable<OAPDeliveryRateDetails> getAll() throws IOException {

        Iterable<OAPDeliveryRateDetails> oapDeliveryRateDetails = this.oapDeliveryRateDetailsRepository.findAll();

        ExcelUtils.writeToExcel(
                (List<OAPDeliveryRateDetails>) oapDeliveryRateDetails, fileName, this.getPath(), "all");

        return oapDeliveryRateDetails;
    }

    /**
     * get all delivery rates by id
     *
     * @param id : id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryRateDetails> getByIdentifiant(String id) throws IOException {

        List<OAPDeliveryRateDetails> oapDeliveryRateDetails = this.getDRDetails(id);

        ExcelUtils.writeToExcel(oapDeliveryRateDetails, fileName, this.getPath(), id);

        return oapDeliveryRateDetails;
    }

    /**
     * get all delivery rates by id which are corrupt
     *
     * @param id : id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryRateDetails> getCorruptPrice(String id) throws IOException {

        List<OAPDeliveryRateDetails> oapDeliveryRateDetails = this.getDRDetails(id);

        Optional<List<OAPDeliveryRateDetails>> oapCorruptDRD =
                Optional.of(oapDeliveryRateDetails
                        .stream()
                        .filter(x -> {
                            long curentDateLong =
                                    Long.parseLong(new SimpleDateFormat("yyyyMMdd")
                                            .format(new Date(System.currentTimeMillis())), 10);
                            if(x.getDateFin() == null || curentDateLong < x.getDateFin()){
                                if(x.getPrixAchatUnitaireHt() != null && x.getPrixVenteUnitaireHt() != null){
                                    return x.getPrixAchatUnitaireHt() > x.getPrixVenteUnitaireHt();
                                }else return false;
                            }else return false;
                        })
                        .collect(Collectors.toList()));

        if (!oapCorruptDRD.isPresent())
            throw new NotFoundException(this.notFoundMessage.toString(id));

        ExcelUtils.writeToExcel(oapCorruptDRD.get(), fileName, this.getPath(), id);
        return oapCorruptDRD.get();
    }

    /**
     * Get all delivery rates by wording
     *
     * @param id : id
     * @param libelleId : libelleId
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryRateDetails> getByLibelleId(String id, String libelleId) throws IOException {
        Optional<List<OAPDeliveryRateDetails>> oapDeliveryRateDetails =
                this.oapDeliveryRateDetailsRepository.findByIdentifiantLikeAndLibelleIdentifiantLike(id, libelleId);

        if (!oapDeliveryRateDetails.isPresent())
            throw new NotFoundException(this.notFoundMessage.toString(id, libelleId));

        ExcelUtils.writeToExcel(oapDeliveryRateDetails.get(), fileName, this.getPath(), id + "#" + libelleId);
        return oapDeliveryRateDetails.get();
    }

    /**
     * Get all delivery rates by designation detail
     *
     * @param id : id
     * @param designation : designation
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    public List<OAPDeliveryRateDetails> getByDesignation(String id, String designation) throws IOException {

        Optional<List<OAPDeliveryRateDetails>> oapDeliveryRateDetails =
                this.oapDeliveryRateDetailsRepository.findByIdentifiantLikeAndDesignationLike(id, designation);

        if (!oapDeliveryRateDetails.isPresent())
            throw new NotFoundException(this.notFoundMessage.toString(id, designation));

        ExcelUtils.writeToExcel(oapDeliveryRateDetails.get(), fileName, this.getPath(), id + "#" + designation);
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

    /**
     * get all delivery rates by id
     *
     * @param id : id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    private List<OAPDeliveryRateDetails> getDRDetails(String id) {
        Optional<List<OAPDeliveryRateDetails>> oapDeliveryRateDetails =
                this.oapDeliveryRateDetailsRepository.findByIdentifiantLike(id);

        if (!oapDeliveryRateDetails.isPresent())
            throw new NotFoundException(this.notFoundMessage.toString(id));

        return oapDeliveryRateDetails.get();
    }
}
