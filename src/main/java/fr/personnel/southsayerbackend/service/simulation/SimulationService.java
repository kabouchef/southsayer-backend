package fr.personnel.southsayerbackend.service.simulation;

import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import fr.personnel.southsayerbackend.model.simulation.PriceLine;
import fr.personnel.southsayerbackend.service.simulation.core.ExtractFromDatabaseService;
import fr.personnel.southsayerbackend.service.simulation.core.XmlToExcelService;
import fr.personnel.southsayerbackend.service.simulation.core.XmlWriterService;
import fr.personnel.southsayerbackend.utils.*;
import fr.personnel.southsayerdatabase.repository.simulation.ConfigurationStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * @author Farouk KABOUCHE
 * 
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

    private final ConfigurationStorageRepository configurationStorageRepository;


    public List<PriceLine> getSimulationOffer(String simulationCode, String environment, String databaseEnvSchema) {
        /**
         * Init
         */
        String target = RestConstantUtils.XML_EXTENSION + "/" + environment + "/" + databaseEnvSchema;
        List<PriceLine> tabPriceElement = null;
        /**
         * Removal of unnecessary spaces
         */
        simulationCode = simulationCode.replace(" ", "");
        /**
         * Drain static repository
         */
        this.deleteFileUtils.DeleteFilesByPath(
                RestConstantUtils.STATIC_DIRECTORY, target, RestConstantUtils.XML_EXTENSION, simulationCode);

        try {
            /**
             * Extract data from DataBase
             */
            String clobFromDatabase = this.extractFromDatabaseService.getClobFromDatabase(simulationCode);

            /**
             * Create file XML_CONF.xml from data extracted
             */
            this.xmlWriterService.generateXML(
                    clobFromDatabase, RestConstantUtils.STATIC_DIRECTORY,
                    environment, databaseEnvSchema, simulationCode);

            /**
             * Create Excel File PRICE_FROM_simulationCode.xls
             * File which present price lines of the simulation
             */
            tabPriceElement = this.xmlToExcelService.generateExcel(
                    simulationCode, RestConstantUtils.STATIC_DIRECTORY, environment, databaseEnvSchema);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return tabPriceElement;
    }

    public int countAllByConfCategIdLikeConfIdLike(String confCategId, String confId){
        return this.configurationStorageRepository.countByConfCategIdLikeAndConfIdLike(confCategId, confId);
    }
}
