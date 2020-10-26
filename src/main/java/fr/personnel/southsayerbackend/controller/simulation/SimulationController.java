package fr.personnel.southsayerbackend.controller.simulation;

import fr.personnel.southsayerbackend.model.simulation.*;
import fr.personnel.southsayerbackend.service.simulation.SimulationService;
import fr.personnel.southsayerbackend.service.simulation.core.ExtractFromDatabaseService;
import fr.personnel.southsayerbackend.utils.global.ExportFileUtils;
import fr.personnel.southsayerbackend.utils.global.StaticPathUtils;
import fr.personnel.southsayerbackend.utils.xml.XmlUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.SIMULATION_PATH;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * @author Farouk KABOUCHE
 * API to extract OAP simulation
 * @version 1.0
 * @see SimulationService
 */
@Slf4j
@RestController
@RequestMapping(SIMULATION_PATH)
@Data
@Transactional
public class SimulationController {

    private final ExportFileUtils exportFileUtils;
    private final SimulationService simulationService;
    private final ExtractFromDatabaseService extractFromDatabaseService;
    private final StaticPathUtils staticPathUtils;
    private final XmlUtils xmlUtils;

    /**
     * Get price lines about simulationCode
     *
     * @param simulationCode : simulation code
     * @return {@link List<PriceLine>}
     */
    @Operation(summary = "API to extract OAP simulation",
            description = "Retrieves the list of simulation price lines according to the simulationCode")
    @CrossOrigin
    @GetMapping("/priceLines")
    public List<PriceLine> getPriceLinesSimulation(
            @Parameter(description = "simulationCode", example = "20191011S49954", required = true)
            @RequestParam(name = "simulationCode") String simulationCode)
            throws IOException {
        return this.simulationService.getSimulationOffer(simulationCode);
    }


    /**
     * Get price_from_simulationCode.xls after the GetMapping "/request"
     *
     * @return {@link ResponseEntity<Resource>}
     */
    @Operation(summary = "API to extract OAP simulation",
            description = "Get the xls file of the simulation submitted by the request '/request'")
    @GetMapping("/download/{extension}/{staticDirectory}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final String extension,
                                                 @PathVariable final String staticDirectory) {

        File directory = new File(this.staticPathUtils.getPath(extension, staticDirectory) + "/");
        File[] listFiles = directory.listFiles();

        String completePath = "";
        for(File item : listFiles) {
            if (item.isFile()) {
                completePath = directory + "/" + item.getName();
            }
        }

        File file = new File(completePath);

        // Load file as Resource
        Resource resource = this.exportFileUtils.loadFileAsResource(file.getAbsolutePath());

        String contentType = "application/octet-stream";

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * Retrieves the list of code simulations by iDOAP
     *
     * @param idOAP : id of OAP
     * @return {@link List<String>}
     */
    @Operation(summary = "API to extract OAP simulation",
            description = "Retrieves the list of code simulations by idOAP")
    @CrossOrigin
    @GetMapping("/byIdOAP")
    @ResponseBody
    public List<String> getSimulationCodes(
            @Parameter(description = "idOAP", example = "OAP:016", required = true)
            @RequestParam(name = "idOAP") String idOAP) {

        return this.simulationService.getSimCodeByConfCategIdLike(idOAP);
    }

    /**
     * Retrieves the list of code simulations which contain the character string sought.
     *
     * @param inputRate : inputRate
     * @return {@link List<String>}
     */
    @Operation(summary = "API to extract OAP simulation",
            description = "Retrieves the list of code simulations which contain the character string sought.")
    @CrossOrigin
    @PostMapping("/search")
    @ResponseBody
    public List<ValueXmlSimulation> getSimulationsBySequenceChar(@RequestBody InputRate inputRate) {
        return this.extractFromDatabaseService.findSimulationBySequenceChar(inputRate);
    }

    /**
     * Returns the value returned by the xpath for each simulation.
     *
     * @param xpathDefinitions : Definition of xpath which permit to find value searched in xml's offer
     * @return {@link List<ValueXmlSimulation>}
     */
    @Operation(summary = "API to extract OAP simulation",
            description = "Returns the value returned by the xpath for each simulation.")
    @CrossOrigin
    @PostMapping("/findByCPE")
    public List<List<ValueXmlSimulation>> getValueInSimulationByCPE(@RequestBody List<XpathDefinition> xpathDefinitions) {
        return xpathDefinitions
                .stream()
                .map(x -> this.extractFromDatabaseService.findValueInSimulationByXpath(x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the value returned by the xpath for each simulation.
     * @param simulationCodes : List of simulationCode
     * @param xpath : xpath
     * @return {@link List<ValueXmlSimulation>}
     */
    @Operation(summary = "API to extract OAP simulation",
            description = "Returns the value returned by the xpath for each simulation.")
    @CrossOrigin
    @PostMapping("/findValuesByCPE")
    public List<ValueXmlSimulation> getValuesInSimByCPE(
            @RequestBody List<String> simulationCodes,
            @Parameter(description = "xpath", example = "//*[contains(@cpe, 'fpModeVente')]//textValue", required = true)
            @RequestParam String xpath) throws IOException {
        return this.simulationService.findValuesSimCodeByXpath(simulationCodes, xpath);
    }

    /**
     * Count simulations by idOAP
     *
     * @param confCategId : idOAP
     * @param confId      : simulation code
     * @return {@link int}
     */
    @Operation(summary = "API to extract OAP simulation", description = "Count simulations by idOAP")
    @CrossOrigin
    @GetMapping("/count")
    public int countAllByConfCategIdLikeConfIdLike(
            @Parameter(description = "idOAP", example = "OAP:016", required = true)
            @RequestParam String confCategId,
            @Parameter(description = "simulationCode", example = "20200%", required = true)
            @RequestParam String confId) {
        return this.simulationService.countAllByConfCategIdLikeConfIdLike(confCategId, confId);
    }

    /**
     * Conversion rate by confCategId, by idOAP and by CPE
     * @param inputRate : input Rate (XpathDefinition, valueSearched)
     * @return {@link ConversionRate}
     */
    @Operation(summary = "API to extract OAP simulation", description = "Get conversion Rate by value searched")
    @CrossOrigin
    @PostMapping("/conversionRate")
    public ConversionRate getConversionRateByInputRate(@RequestBody InputRate inputRate) throws IOException {
        return this.simulationService.conversionRateByInputRate(inputRate);
    }

    /**
     * Convert Offer to RT
     * @param simulationCode : simulationCode
     * @return {@link ConversionRate}
     */
    @Operation(summary = "API to extract OAP simulation", description = "Convert Offer to RT")
    @CrossOrigin
    @PostMapping("/convertToRT")
    public String convertOfferToRT(
            @Parameter(description = "simulationCode", example = "20200810S52546", required = true)
            @RequestParam String simulationCode) {
        return this.simulationService.convertSimulationToRT(simulationCode);
    }

    /**
     * Update value in xml simulation
     *
     * @param updateValueDTOS : List<updateValueDTO>
     * @return {@link List<String>}
     */
    @Operation(summary = "API to manage OAP Activity Codes", description = "Update value in xml simulation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Technical error happened")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("updateXmlConf")
    public List<String> updateConfigurationStorage(
            @Parameter(
                    description = "xpath",
                    example = "//*[@cpe='CPE.Settings.Session.Debug']/@value",
                    required = true)
            @RequestBody final List<UpdateValueDTO> updateValueDTOS) {
        return this.simulationService.save(updateValueDTOS);
    }

    // TODO 1 - Make frontUI for uploadToServer
    /**
     * Upload To Server
     * @param simulationCode : simulationCode
     * @param target : target
     * @return {@link boolean}
     * @throws IOException : IOException
     */
    @Operation(summary = "API to extract OAP simulation", description = "Upload To Server")
    @CrossOrigin
    @PostMapping("/uploadToServer")
    public List<PriceLine> uploadToServer(
            @Parameter(description = "simulationCode", example = "20191011S49954", required = true)
            @RequestParam String simulationCode,
            @Parameter(description = "target", example = "/home1/edge/conf/run/settings/configurator", required = true)
            @RequestParam String target) throws IOException {

        List<PriceLine> priceLines = this.simulationService.getSimulationOffer(simulationCode);

        this.xmlUtils.replaceValueIntoXMLFileByXpath(
                simulationCode,
                "//*[@cpe='CPE.Settings.Session.Debug']/@value",
                "true");

        this.simulationService.uploadToSFTPServer(simulationCode, target);

        return priceLines;
    }

    /**
     * Download To Server
     * @param simulationCode : simulationCode
     * @param target : target
     * @return {@link boolean}
     * @throws IOException : IOException
     */
    @Operation(summary = "API to extract OAP simulation", description = "Download To Server")
    @CrossOrigin
    @PostMapping("/downloadToServer")
    public boolean downloadToServer(
            @Parameter(description = "simulationCode", example = "20191011S49954", required = true)
            @RequestParam String simulationCode,
            @Parameter(description = "target", example = "/home1/edge/conf/run/settings/configurator", required = true)
            @RequestParam String target) throws IOException {
        return this.simulationService.downloadToSFTPServer(simulationCode, target);
    }
}
