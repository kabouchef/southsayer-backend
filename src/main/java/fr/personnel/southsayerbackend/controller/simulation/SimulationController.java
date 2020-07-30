package fr.personnel.southsayerbackend.controller.simulation;

import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import fr.personnel.southsayerbackend.model.simulation.PriceLine;
import fr.personnel.southsayerbackend.model.simulation.ValueXmlSimulation;
import fr.personnel.southsayerbackend.model.simulation.XpathDefinition;
import fr.personnel.southsayerbackend.model.simulation.rate.ConversionRate;
import fr.personnel.southsayerbackend.model.simulation.rate.InputRate;
import fr.personnel.southsayerbackend.service.simulation.SimulationService;
import fr.personnel.southsayerbackend.service.simulation.core.ExtractFromDatabaseService;
import fr.personnel.southsayerbackend.utils.ExportFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.*;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * @author Farouk KABOUCHE
 * API to extract OAP simulation prices
 * @version 1.0
 * @see SimulationService
 */
@Slf4j
@Api("API to extract OAP simulation prices")
@RestController
@RequestMapping(SIMULATION_PATH)
@Data
@Transactional
public class SimulationController {

    private final ExportFileUtils exportFileUtils;
    private final SimulationService simulationService;
    private final ExtractFromDatabaseService extractFromDatabaseService;

    @Value("${ENVIRONMENT}")
    private String environment;
    @Value("${DATABASE_ENV_SCHEMA}")
    private String databaseEnvSchema;


    /**
     * Get price lines about simulationCode
     *
     * @param simulationCode : simulation code
     * @return {@link List<PriceLine>}
     */
    @ApiOperation(value = "Retrieves the list of simulation price lines according to the simulationCode")
    @CrossOrigin
    @GetMapping
    public List<PriceLine> getPriceLinesSimulation(@RequestParam(name = "simulationCode") String simulationCode)
            throws IOException {
        return this.simulationService.getSimulationOffer(simulationCode, environment, databaseEnvSchema);
    }


    /**
     * Get price_from_simulationCode.xls after the GetMapping "/request"
     *
     * @param simulationCode : simulation code
     * @return {@link ResponseEntity<Resource>}
     */
    @ApiOperation(value = "Get the xls file of the simulation submitted by the request '/request'")
    @GetMapping("/downloadPricesFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam(name = "simulationCode") String simulationCode) {
        String target = STATIC_DIRECTORY_FILES + "/" + XLS_EXTENSION + "/" + environment + "/" + databaseEnvSchema + "/";

        File file = new File(target + "PRICE_FROM_" + simulationCode + "." + XLS_EXTENSION);

        // Load file as Resource
        Resource resource = this.exportFileUtils.loadFileAsResource(file.getAbsolutePath());

        String contentType = "application/octet-stream";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    /**
     * Retrieves the list of code simulations by iDOAP
     *
     * @param idOAP : id of OAP
     * @return {@link List<String>}
     */
    @ApiOperation(value = "Retrieves the list of code simulations by iDOAP")
    @CrossOrigin
    @GetMapping("/byIdOAP")
    @ResponseBody
    public List<String> getSimulationCodes(@RequestParam(name = "idOAP") String idOAP) {

        return this.simulationService.getSimCodebyConfCategIdLike(idOAP);
    }

    /**
     * Retrieves the list of code simulations which contain the character string sought.
     *
     * @param idOAP        : id of OAP
     * @param sequenceChar : Characters sequence searched
     * @return {@link List<String>}
     */
    @ApiOperation(value = "Retrieves the list of code simulations which contain the character string sought.")
    @CrossOrigin
    @PostMapping("/search")
    @ResponseBody
    public List<ValueXmlSimulation> getSimulationsBySequenceChar(@RequestBody InputRate inputRate) {
        return this.extractFromDatabaseService.findSimulationBySequenceChar(inputRate);
    }

    /**
     * Returns the value returned by the xpath for each simulation.
     *
     * @param xpathDefinition : Definition of xpath which permit to find value searched in xml's offer
     * @return {@link List<ValueXmlSimulation>}
     */
    @ApiOperation(value = "Returns the value returned by the xpath for each simulation.")
    @CrossOrigin
    @PostMapping("/findByCPE")
    public List<ValueXmlSimulation> getValueInSimulationByCPE(@RequestBody XpathDefinition xpathDefinition) {

        return this.extractFromDatabaseService.findValueInSimulationByXpath(xpathDefinition);
    }

    /**
     * Count simulations by idOAP
     *
     * @param confCategId : idOAP
     * @param confId      : simulation code
     * @return {@link int}
     */
    @ApiOperation(value = "Count simulations by idOAP")
    @CrossOrigin
    @GetMapping("/count")
    public int countAllByConfCategIdLikeConfIdLike(@RequestParam String confCategId, @RequestParam String confId) {
        return this.simulationService.countAllByConfCategIdLikeConfIdLike(confCategId, confId);
    }

    /**
     * Conversion rate by confCategId, by idOAP and by CPE
     * @param inputRate : input Rate (XpathDefinition, valueSearched)
     * @return {@link ConversionRate}
     */
    @ApiOperation(value = "Get conversion Rate by value searched")
    @CrossOrigin
    @PostMapping("/conversionRate")
    public ConversionRate getConversionRateByInputRate(@RequestBody InputRate inputRate) {
        return this.simulationService.conversionRateByInputRate(inputRate);
    }


}
