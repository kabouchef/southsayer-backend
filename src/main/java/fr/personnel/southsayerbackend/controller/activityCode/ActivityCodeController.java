package fr.personnel.southsayerbackend.controller.activityCode;

import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import fr.personnel.southsayerbackend.service.activityCode.ActivityCodeService;
import fr.personnel.southsayerdatabase.entity.activityCode.ActivityCode;
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
 * API to manage OAP Activity Codes
 */
@Slf4j
@Api("API to manage OAP Activity Codes")
@RestController
@RequestMapping(ActivityCodeController.PATH)
@RequiredArgsConstructor
@Transactional
public class ActivityCodeController {

    public final static String PATH = RestConstantUtils.DEFAULT_PATH + "/activity-code";

    private final ActivityCodeService activityCodeService;

    /**
     * Get OAP AC
     *
     * @return {@link Iterable<ActivityCode>}
     */
    @ApiOperation(value = "Get all OAP AC")
    @CrossOrigin
    @GetMapping
    public Iterable<ActivityCode> getAllActivityCode() {
        return this.activityCodeService.getAll();
    }

    /**
     * Get OAP AC by code activite
     *
     * @param codActivite : codActivite
     * @return {@link List<ActivityCode>}
     */
    @ApiOperation(value = "Get OAP AC by code activite")
    @CrossOrigin
    @GetMapping("/byCodActivite")
    public List<ActivityCode> getByCodActivite(@RequestParam final String codActivite) {
        return this.activityCodeService.getByCodActivite(codActivite);
    }

    /**
     * Get OAP AC by Lib1
     *
     * @param lib1 : lib1
     * @return {@link List<ActivityCode>}
     */
    @ApiOperation(value = "Get OAP AC by Lib1")
    @CrossOrigin
    @GetMapping("/byLib1")
    public List<ActivityCode> getByLib1(@RequestParam final String lib1) {
        return this.activityCodeService.getByLib1(lib1);
    }

    /**
     * Get OAP AC by idRayon
     *
     * @param idRayon : idRayon
     * @return {@link List<ActivityCode>}
     */
    @ApiOperation(value = "Get OAP AC by IdRayon")
    @CrossOrigin
    @GetMapping("/byIdRayon")
    public List<ActivityCode> getByIdRayon(@RequestParam final Long idRayon) {
        return this.activityCodeService.getByIdRayon(idRayon);
    }

    /**
     * Get OAP AC by rayon
     *
     * @param rayon : rayon
     * @return {@link List<ActivityCode>}
     */
    @ApiOperation(value = "Get OAP AC by rayon")
    @CrossOrigin
    @GetMapping("/byRayon")
    public List<ActivityCode> getByRayon(@RequestParam final String rayon) {
        return this.activityCodeService.getByRayon(rayon);
    }

    /**
     * Post OAP AC
     *
     * @param activityCodes : OAP AC
     * @return {@link List<ActivityCode>}
     */
    @ApiOperation(value = "Add OAP AC")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 500, message = "Technical error happened")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public List<ActivityCode> addActivityCode(@RequestBody final List<ActivityCode> activityCodes) {
        return this.activityCodeService.save(activityCodes);
    }

    /**
     * Delete AC
     *
     * @param listCodActivite : list CodActivite
     */
    @ApiOperation(value = "Delete OAP AC")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Deleted")})
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivityCode(@RequestParam final List<String> listCodActivite) {
        this.activityCodeService.deleteByCodActivite(listCodActivite);
    }
}
