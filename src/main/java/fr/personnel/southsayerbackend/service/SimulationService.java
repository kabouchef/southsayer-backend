package fr.personnel.southsayerbackend.service;

import fr.personnel.southsayerbackend.model.PriceLine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
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
    private final DeleteFileService deleteFileService;
    private final XmlWriterService xmlWriterService;
    private final XMLToExcelService xmlToExcelService;


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
        this.deleteFileService.DeleteFilesByPath(staticDir, target, extension, simulationCode);

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
