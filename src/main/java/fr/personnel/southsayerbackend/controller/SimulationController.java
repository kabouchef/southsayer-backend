package fr.personnel.southsayerbackend.controller;

import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import fr.personnel.southsayerbackend.model.PriceLine;
import fr.personnel.southsayerbackend.model.XpathDefinition;
import fr.personnel.southsayerbackend.service.ExportFileService;
import fr.personnel.southsayerbackend.service.ExtractFromDatabaseService;
import fr.personnel.southsayerbackend.service.SimulationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Slf4j
@Api("API to extract OAP simulation prices")
@RestController
@RequestMapping(SimulationController.PATH)
@RequiredArgsConstructor
public class SimulationController {

    public final static String PATH = RestConstantUtils.DEFAULT_PATH + "/simulation";

    private final ExportFileService exportFileService;
    private final SimulationService simulationService;
    private final ExtractFromDatabaseService extractFromDatabaseService;

    @Value("${ENVIRONMENT}")
    private String environment;
    @Value("${DATABASE_ENV_SCHEMA}")
    private String databaseEnvSchema;


    /**
     * Get price lines about simulationCode and environment
     *
     * @param simulationCode : simulation code
     * @return {@link List<PriceLine>}
     */
    @ApiOperation(value = "Retrieves the list of simulation price lines according to the simulationCode and the environment")
    @CrossOrigin
    @GetMapping()
    @ResponseBody
    public List<PriceLine> getSimulation(@RequestParam(name = "simulationCode") String simulationCode) {
        return this.simulationService.getSimulationOffer(simulationCode, environment, databaseEnvSchema);
    }


    /**
     * Get price_from_simulationCode.xls after the GetMapping "/request"
     *
     * @param simulationCode : simulation code
     * @return {@link ResponseEntity<Resource>}
     */
    @ApiOperation(value = "Get the xls file of the simulation submitted by the request '/request'")
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam(name = "simulationCode") String simulationCode) {
        File file = new File("src/main/resources/static/xls/PRICE_FROM_" + simulationCode + ".xls");

        // Load file as Resource
        Resource resource = this.exportFileService.loadFileAsResource(file.getAbsolutePath());

        String contentType = "application/octet-stream";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    /**
     * Retrieves the list of code simulations which contain the character string sought.
     *
     * @param environment  : environment of database
     * @param schema       : schema of database
     * @param idOAP        : id of OAP
     * @param sequenceChar : Characters sequence searched
     * @return {@link List<String>}
     * @throws SQLException : Retrieves SQL exceptions
     */
    @ApiOperation(value = "Retrieves the list of code simulations which contain the character string sought.")
    @CrossOrigin
    @GetMapping("/find")
    @ResponseBody
    public List<String> getSimultionsBySequenceChar(@RequestParam(name = "environment") String environment, @RequestParam(name = "schema") String schema, @RequestParam(name = "idOAP") String idOAP, @RequestParam(name = "sequenceChar") String sequenceChar) throws SQLException {

        return this.extractFromDatabaseService.findSimulationBySequenceChar(idOAP, sequenceChar);
    }

    /**
     * Returns the value returned by the xpath for each simulation.
     *
     * @param xpathDefinition : Definition of xpath which permit to find value searched in xml's offer
     * @return {@link Hashtable}
     * @throws SQLException : Retrieves SQL exceptions
     */
    @ApiOperation(value = "Returns the value returned by the xpath for each simulation.")
    @CrossOrigin
    @PostMapping("/findByCPE")
    public Map<String, String> getValueInSimulationByCPE(@RequestBody XpathDefinition xpathDefinition) throws SQLException {

        return this.extractFromDatabaseService.findValueInSimulationByXpath(xpathDefinition.getIdOAP(), xpathDefinition.getXpath());
    }


}
