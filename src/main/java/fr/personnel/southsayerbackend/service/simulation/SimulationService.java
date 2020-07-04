package fr.personnel.southsayerbackend.service.simulation;

import fr.personnel.southsayerbackend.model.simulation.PriceLine;
import fr.personnel.southsayerbackend.service.simulation.core.ExtractFromDatabaseService;
import fr.personnel.southsayerbackend.service.simulation.core.XmlToExcelService;
import fr.personnel.southsayerbackend.service.simulation.core.XmlWriterService;
import fr.personnel.southsayerbackend.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Simulation Offer Service
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final ExtractFromDatabaseService extractFromDatabaseService;
    private final DeleteFileUtils deleteFileUtils;
    private final XmlWriterService xmlWriterService;
    private final XmlToExcelService xmlToExcelService;


    public List<PriceLine> getSimulationOffer(String simulationCode, String environment, String databaseEnvSchema) {
        /**
         * Init
         */
        String staticDir = "src/main/resources/static/";
        String extension = "xml";
        String target = extension + "/" + environment + "/" + databaseEnvSchema;
        List<PriceLine> tabPriceElement = null;
        /**
         * Removal of unnecessary spaces
         */
        simulationCode = simulationCode.replace(" ", "");
        /**
         * Drain static repository
         */
        this.deleteFileUtils.DeleteFilesByPath(staticDir, target, extension, simulationCode);

        try {
            /**
             * Extract data from DataBase
             */
            String clobFromDatabase = this.extractFromDatabaseService.getClobFromDatabase(simulationCode);

            /**
             * Create file XML_CONF.xml from data extracted
             */
            this.xmlWriterService.generateXML(clobFromDatabase, staticDir, environment, databaseEnvSchema, simulationCode);

            /**
             * Create Excel File PRICE_FROM_simulationCode.xls
             * File which present price lines of the simulation
             */
            tabPriceElement = this.xmlToExcelService.generateExcel(simulationCode, staticDir, environment, databaseEnvSchema);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return tabPriceElement;
    }
}
