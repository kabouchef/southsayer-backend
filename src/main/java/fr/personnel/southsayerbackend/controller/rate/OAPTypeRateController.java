package fr.personnel.southsayerbackend.controller.rate;

import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import fr.personnel.southsayerbackend.service.rate.OAPTypeRateService;
import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Farouk KABOUCHE
 * <p>
 * API to manage OAP delivery type
 */
@Slf4j
@Api("API to manage OAP delivery type")
@RestController
@RequestMapping(OAPTypeRateController.PATH)
@RequiredArgsConstructor
@Transactional
public class OAPTypeRateController {

    static final String PATH = RestConstantUtils.DEFAULT_PATH + "/delivery-type";

    private final OAPTypeRateService oapTypeRateService;

    /**
     * Get all OAP DT
     *
     * @return {@link Iterable<OAPDeliveryType>}
     */
    @ApiOperation(value = "Get all OAP DT")
    @CrossOrigin
    @GetMapping
    public Iterable<OAPDeliveryType> getAllDT() {
        return this.oapTypeRateService.getAll();
    }

    /**
     * Get all OAP DT by Code type prestation
     *
     * @param codTypePrestation : Code Type Prestation
     * @return {@link List<OAPDeliveryType>}
     */
    @ApiOperation(value = "Get all OAP DT by Code type prestation")
    @CrossOrigin
    @GetMapping("/byCodTypePrestation")
    public List<OAPDeliveryType> getRateLineByCodTypePrestation(
            @RequestParam final String codTypePrestation) {
        return this.oapTypeRateService.getByCodTypePrestation(codTypePrestation);
    }

    /**
     * Get all OAP DT by Libelle Type Prestation
     *
     * @param wording : wording
     * @return {@link List<OAPDeliveryType>}
     */
    @ApiOperation(value = "Get all OAP DT by Libelle Type Prestation")
    @CrossOrigin
    @GetMapping("/byWording")
    public List<OAPDeliveryType> getRateLineByWording(
            @RequestParam final String wording) {
        return this.oapTypeRateService.getByWordingDT(wording);
    }

    /**
     * Get all OAP DT by idOAP
     *
     * @param idOAP : Code Type Prestation
     * @return {@link List<OAPDeliveryType>}
     */
    @ApiOperation(value = "Get all by idOAP")
    @CrossOrigin
    @GetMapping("/byIdOAP")
    public List<OAPDeliveryType> getRateLineByIdOAP(
            @RequestParam final Long idOAP) {
        return this.oapTypeRateService.getByIdOAP(idOAP);
    }

    /**
     * Add OAP DT
     *
     * @param oapDeliveryType : Delivery Type to add
     * @return {@link List<OAPDeliveryType>}
     */
    @ApiOperation(value = "Add OAP DT")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Added"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Technical error happened")})
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
    @ApiOperation(value = "Delete OAP DT")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted")})
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDT(@RequestParam final List<String> codTypePrestation) {
        this.oapTypeRateService.deleteByCodTypePrestation(codTypePrestation);
    }


}
