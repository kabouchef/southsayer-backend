package fr.personnel.southsayerbackend.controller.rate;

import fr.personnel.southsayerbackend.service.rate.OAPTypeRateService;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.TYPE_RATE_PATH;

/**
 * @author Farouk KABOUCHE
 * API to manage OAP delivery type
 * @version 1.0
 * @see OAPTypeRateService
 */
@Slf4j
@RestController
@RequestMapping(TYPE_RATE_PATH)
@RequiredArgsConstructor
@Transactional
public class OAPTypeRateController {

    private final OAPTypeRateService oapTypeRateService;

    /**
     * Get all OAP DT
     *
     * @return {@link Iterable<OAPDeliveryType>}
     */
    @Operation(summary = "API to manage OAP delivery type", description = "Get all OAP DT")
    @CrossOrigin
    @GetMapping
    public Iterable<OAPDeliveryType> getAllDT() throws IOException {
        return this.oapTypeRateService.getAll();
    }

    // TODO 4 - Make frontUI for getDTByCodTypePrestation
    /**
     * Get all OAP DT by Code type prestation
     *
     * @param codTypePrestation : Code Type Prestation
     * @return {@link List<OAPDeliveryType>}
     */
    @Operation(summary = "API to manage OAP delivery type", description = "Get all OAP DT by Code type prestation")
    @CrossOrigin
    @GetMapping("/byCodTypePrestation")
    public List<OAPDeliveryType> getDTByCodTypePrestation(
            @RequestParam final String codTypePrestation) throws IOException {
        return this.oapTypeRateService.getByCodTypePrestation(codTypePrestation);
    }

    /**
     * Get all OAP DT by Libelle Type Prestation
     *
     * @param wording : wording
     * @return {@link List<OAPDeliveryType>}
     */
    @Operation(summary = "API to manage OAP delivery type", description = "Get all OAP DT by Libelle Type Prestation")
    @CrossOrigin
    @GetMapping("/byWording")
    public List<OAPDeliveryType> getDTByWording(
            @RequestParam final String wording) throws IOException {
        return this.oapTypeRateService.getByWordingDT(wording);
    }

    /**
     * Get all OAP DT by idOAP
     *
     * @param idOAP : Code Type Prestation
     * @return {@link List<OAPDeliveryType>}
     */
    @Operation(summary = "API to manage OAP delivery type", description = "Get all by idOAP")
    @CrossOrigin
    @GetMapping("/byIdOAP")
    public List<OAPDeliveryType> getDTByIdOAP(
            @RequestParam final Long idOAP) throws IOException {
        return this.oapTypeRateService.getByIdOAP(idOAP);
    }

    /**
     * Add OAP DT
     *
     * @param oapDeliveryType : Delivery Type to add
     * @return {@link List<OAPDeliveryType>}
     */
    @Operation(summary = "API to manage OAP delivery type", description = "Add OAP DT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Technical error happened")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public List<OAPDeliveryType> addDT(
            @RequestBody final List<OAPDeliveryType> oapDeliveryType) {
        return this.oapTypeRateService.save(oapDeliveryType);
    }

    /**
     * Delete OAP DT
     *
     * @param codTypePrestation : Code Type Prestation
     */
    @Operation(summary = "API to manage OAP delivery type", description = "Delete OAP DT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "500", description = "Technical error happened")})
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDT(@RequestParam final List<String> codTypePrestation) {
        this.oapTypeRateService.deleteByCodTypePrestation(codTypePrestation);
    }


}
