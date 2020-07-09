package fr.personnel.southsayerbackend.controller.rate;

import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import fr.personnel.southsayerbackend.service.rate.OAPDeliveryRateService;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryRateDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Farouk KABOUCHE
 *
 * API to manage OAP delivery rate lines
 */
@Slf4j
@Api("API to manage OAP delivery rate lines")
@RestController
@RequestMapping(OAPDeliveryRateController.PATH)
@RequiredArgsConstructor
@Transactional
public class OAPDeliveryRateController {

    public final static String PATH = RestConstantUtils.DEFAULT_PATH + "/rate";

    private final OAPDeliveryRateService OAPDeliveryRateService;

    /**
     * Get OAP DR by id
     *
     * @param id : id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @ApiOperation(value = "Get OAP DR by id")
    @CrossOrigin
    @GetMapping
    public List<OAPDeliveryRateDetails> getRateLineById(@RequestParam final String id) {
        return this.OAPDeliveryRateService.getByIdentifiant(id);
    }

    /**
     * Get OAP DR by wording
     *
     * @param id        : id
     * @param libelleId : libelle id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @ApiOperation(value = "Get OAP DR by wording")
    @CrossOrigin
    @GetMapping("/byLibelleId")
    public List<OAPDeliveryRateDetails> getRateLineByLibelleId(@RequestParam final String id, @RequestParam final String libelleId) {
        return this.OAPDeliveryRateService.getByLibelleId(id, libelleId);
    }

    /**
     * Get OAP DR by designation detail
     *
     * @param id          : id
     * @param designation : designation of delivery detail
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @ApiOperation(value = "Get OAP DR by designation detail")
    @CrossOrigin
    @GetMapping("/byDesignation")
    public List<OAPDeliveryRateDetails> getRateLineByDesignation(@RequestParam final String id, @RequestParam final String designation) {
        return this.OAPDeliveryRateService.getByDesignation(id, designation);
    }

    /**
     * Post OAP DR
     *
     * @param oapDeliveryRateDetails : OAP DR
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @ApiOperation(value = "Add a OAPDeliveryRateDetails")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Technical error happened")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public List<OAPDeliveryRateDetails> addRateLine(@RequestBody final List<OAPDeliveryRateDetails> oapDeliveryRateDetails) {
        return this.OAPDeliveryRateService.save(oapDeliveryRateDetails);
    }

    /**
     * Delete DR
     * @param listIdentifiant : list identifiant
     */
    @ApiOperation(value = "Delete")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Deleted")})
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll(@RequestParam final List<String> listIdentifiant) {
        this.OAPDeliveryRateService.deleteByIdentifiant(listIdentifiant);
    }
}
