package fr.personnel.southsayerbackend.controller.rate;

import fr.personnel.southsayerbackend.service.rate.OAPDeliveryRateService;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryRateDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.DELIVERY_RATE_PATH;

/**
 * @author Farouk KABOUCHE
 * API to manage OAP delivery rate lines
 * @version 1.0
 * @see OAPDeliveryRateService
 */
@Slf4j
@RestController
@RequestMapping(DELIVERY_RATE_PATH)
@Data
@Transactional
public class OAPDeliveryRateController {

    private final OAPDeliveryRateService oapDeliveryRateService;

    /**
     * Get all OAP DR
     *
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @Operation(summary = "API to manage OAP delivery rate lines", description = "Get all OAP DR")
    @CrossOrigin
    @GetMapping
    public Iterable<OAPDeliveryRateDetails> getAllDR() throws IOException {
        return this.oapDeliveryRateService.getAll();
    }

    // TODO 2 - Make frontUI for getDRById
    /**
     * Get OAP DR by id
     *
     * @param id : id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @Operation(summary = "API to manage OAP delivery rate lines", description = "Get OAP DR by id")
    @CrossOrigin
    @GetMapping("/byId")
    public List<OAPDeliveryRateDetails> getDRById(
            @Parameter(description = "id", example = "S_%", required = true)
            @RequestParam final String id) throws IOException {
        return this.oapDeliveryRateService.getByIdentifiant(id);
    }

    /**
     * Get OAP DR by wording
     *
     * @param id        : id
     * @param libelleId : libelle id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @Operation(summary = "API to manage OAP delivery rate lines", description = "Get OAP DR by wording")
    @CrossOrigin
    @GetMapping("/byLibelleId")
    public List<OAPDeliveryRateDetails> getDRByLibelleId(
            @RequestParam final String id,
            @RequestParam final String libelleId) throws IOException {
        return this.oapDeliveryRateService.getByLibelleId(id, libelleId);
    }

    /**
     * Get OAP DR by designation detail
     *
     * @param id          : id
     * @param designation : designation of delivery detail
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @Operation(summary = "API to manage OAP delivery rate lines", description = "Get OAP DR by designation detail")
    @CrossOrigin
    @GetMapping("/byDesignation")
    public List<OAPDeliveryRateDetails> getDRByDesignation(
            @RequestParam final String id,
            @RequestParam final String designation) throws IOException {
        return this.oapDeliveryRateService.getByDesignation(id, designation);
    }

    // TODO 3 - Make frontUI for getAllCorruptPrice
    /**
     * Get all OAP DR corrupt by id
     *
     * @param id : id
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @Operation(summary = "API to manage OAP delivery rate lines", description = "Get all OAP DR corrupt by id")
    @CrossOrigin
    @GetMapping("/corruptPrice")
    public List<OAPDeliveryRateDetails> getAllCorruptPrice(
            @Parameter(description = "id", example = "S_%", required = true)
            @RequestParam final String id) throws IOException {
        return this.oapDeliveryRateService.getCorruptPrice(id);
    }

    /**
     * Post OAP DR
     *
     * @param oapDeliveryRateDetails : OAP DR
     * @return {@link List<OAPDeliveryRateDetails>}
     */
    @Operation(summary = "API to manage OAP delivery rate lines", description = "Add OAP DR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Technical error happened")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public List<OAPDeliveryRateDetails> addDR(@RequestBody final List<OAPDeliveryRateDetails> oapDeliveryRateDetails) {
        return this.oapDeliveryRateService.save(oapDeliveryRateDetails);
    }

    /**
     * Delete DR
     *
     * @param listIdentifiant : list identifiant
     */
    @Operation(summary = "API to manage OAP delivery rate lines", description = "Delete OAP DR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "500", description = "Technical error happened")})
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll(@RequestParam final List<String> listIdentifiant) {
        this.oapDeliveryRateService.deleteByIdentifiant(listIdentifiant);
    }
}
