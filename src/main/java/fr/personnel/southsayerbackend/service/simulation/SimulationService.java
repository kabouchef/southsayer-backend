package fr.personnel.southsayerbackend.service.simulation;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerbackend.model.simulation.PriceLine;
import fr.personnel.southsayerbackend.model.simulation.ValueXmlSimulation;
import fr.personnel.southsayerbackend.model.simulation.rate.ConversionRate;
import fr.personnel.southsayerbackend.model.simulation.rate.InputRate;
import fr.personnel.southsayerbackend.service.simulation.core.ExtractFromDatabaseService;
import fr.personnel.southsayerbackend.service.simulation.core.XmlToExcelService;
import fr.personnel.southsayerbackend.service.simulation.core.XmlWriterService;
import fr.personnel.southsayerbackend.utils.MathUtils;
import fr.personnel.southsayerdatabase.entity.simulation.ConfigurationStorage;
import fr.personnel.southsayerdatabase.repository.simulation.ConfigurationStorageRepository;
import jdk.jfr.Percentage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.STATIC_DIRECTORY_FILES;
import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.XML_EXTENSION;

/**
 * @author Farouk KABOUCHE
 * Simulation Offer Service
 * @see ExtractFromDatabaseService
 * @version 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final ExtractFromDatabaseService extractFromDatabaseService;
    private final XmlWriterService xmlWriterService;
    private final XmlToExcelService xmlToExcelService;

    private final ConfigurationStorageRepository configurationStorageRepository;
    private final NotFoundMessage notFoundMessage;


    public List<PriceLine> getSimulationOffer(String simulationCode, String environment, String databaseEnvSchema) throws IOException {
        /**
         * Init
         */
        String path =  STATIC_DIRECTORY_FILES + environment + "/" + databaseEnvSchema + "/" + XML_EXTENSION;
        List<PriceLine> tabPriceElement = null;
        /**
         * Removal of unnecessary spaces
         */
        simulationCode = simulationCode.replace(" ", "");
        /**
         * Drain static repository
         */
        FileUtils.cleanDirectory(new File(path));

        try {
            /**
             * Extract data from DataBase
             */
            String clobFromDatabase = this.extractFromDatabaseService.getClobFromDatabase(simulationCode);

            /**
             * Create file XML_CONF.xml from data extracted
             */
            this.xmlWriterService.generateXML(clobFromDatabase, path, simulationCode);

            /**
             * Create Excel File PRICE_FROM_simulationCode.xls
             * File which present price lines of the simulation
             */
            tabPriceElement = this.xmlToExcelService.generateExcel(simulationCode, environment, databaseEnvSchema);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return tabPriceElement;
    }

    public int countAllByConfCategIdLikeConfIdLike(String confCategId, String confId) {
        return this.configurationStorageRepository.countByConfCategIdLikeAndConfIdLike(confCategId, confId);
    }

    public ConversionRate conversionRateByInputRate(InputRate inputRate) {
        ConversionRate conversionRate = new ConversionRate();
        double totalRate;
        double valueRate;
        double rate;

        List<ValueXmlSimulation> valueXmlSimulations =
                this.extractFromDatabaseService.findValueInSimulationByXpath(inputRate.getXpathDefinition());

        totalRate =
                valueXmlSimulations.stream()
                        .filter(x -> x.getIdOAP().equals(inputRate.getXpathDefinition().getIdOAP()))
                        .map(ValueXmlSimulation::getIdOAP)
                        .count();
        ;
        valueRate =
                valueXmlSimulations.stream()
                        .filter(x -> x.getValue().equals(inputRate.getValueSearched()))
                        .map(ValueXmlSimulation::getIdOAP)
                        .count();

        rate = MathUtils.calculatePercentage(valueRate, totalRate);

        conversionRate.setRating(rate);
        conversionRate.setValueRate(valueRate);
        conversionRate.setTotal(totalRate);

        return conversionRate;
    }

    public List<String> getSimCodebyConfCategIdLike(String confCategId) {
        Optional<List<ConfigurationStorage>> configurationStorages =
                this.configurationStorageRepository.findByConfCategIdLike(confCategId);

        if (!configurationStorages.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(confCategId));

        return configurationStorages.get().stream()
                .filter(x -> x.getConfCategId().contains("OAP:0"))
                .map(ConfigurationStorage::getConfId)
                .collect(Collectors.toList());
    }
}
