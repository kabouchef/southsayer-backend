package fr.personnel.southsayerbackend.service.simulation;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerbackend.model.simulation.PriceLine;
import fr.personnel.southsayerbackend.model.simulation.rate.ConversionRate;
import fr.personnel.southsayerbackend.model.simulation.rate.InputRate;
import fr.personnel.southsayerbackend.service.simulation.core.ExcelConverterService;
import fr.personnel.southsayerbackend.service.simulation.core.ExtractFromDatabaseService;
import fr.personnel.southsayerbackend.service.simulation.core.StaticPathService;
import fr.personnel.southsayerbackend.service.simulation.core.XmlWriterService;
import fr.personnel.southsayerbackend.utils.ExcelUtils;
import fr.personnel.southsayerbackend.utils.MathUtils;
import fr.personnel.southsayerdatabase.entity.simulation.ConfigurationStorage;
import fr.personnel.southsayerdatabase.repository.simulation.ConfigurationStorageRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.*;
import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.STATIC_DIRECTORY_TYPE_RATE;

/**
 * @author Farouk KABOUCHE
 * Simulation Offer Service
 * @see ExtractFromDatabaseService
 * @version 1.0
 */

@Slf4j
@Service
@Data
public class SimulationService {

    private final ExtractFromDatabaseService extractFromDatabaseService;
    private final XmlWriterService xmlWriterService;
    private final ExcelConverterService excelConverterService;

    private final ConfigurationStorageRepository configurationStorageRepository;
    private final NotFoundMessage notFoundMessage;
    private final StaticPathService staticPathService;

    String fileName = "LM - " + this.getClass().getSimpleName().replace("Service","");

    /**
     * Get Path to export file
     * @return {@link String}
     */
    private String getPath(){
        return this.staticPathService.getPath(XLS_EXTENSION, STATIC_DIRECTORY_CONVERSION_RATE);
    }




    public List<PriceLine> getSimulationOffer(String simulationCode)
            throws IOException {


        List<PriceLine> tabPriceElement = null;
        simulationCode = simulationCode.replace(" ", "");
        FileUtils.cleanDirectory(new File(this.staticPathService.getPath(XML_EXTENSION, STATIC_DIRECTORY_SIMULATION)));

        try {
            /**
             * Create file XML_CONF.xml from data extracted
             */
            this.xmlWriterService.generateXML(
                    this.extractFromDatabaseService.getClobFromDatabase(simulationCode),
                    simulationCode);

            /**
             * Create Excel File PRICE_FROM_simulationCode.xls
             * File which present price lines of the simulation
             */
            tabPriceElement = this.excelConverterService.generatePriceLinesExcel(simulationCode);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return tabPriceElement;
    }

    public int countAllByConfCategIdLikeConfIdLike(String confCategId, String confId) {
        return this.configurationStorageRepository.countByConfCategIdLikeAndConfIdLike(confCategId, confId);
    }

    public ConversionRate conversionRateByInputRate(InputRate inputRate)
            throws IOException{

        double totalRate = this.countAllByConfCategIdLikeConfIdLike(
                        inputRate.getXpathDefinition().getIdOAP(),
                        inputRate.getXpathDefinition().getSimulationCode());

        double valueRate = this.extractFromDatabaseService.countSimulationsByValueByXpath(inputRate);

        double rate = MathUtils.calculatePercentage(valueRate, totalRate);

        /*this.excelConverterService
                .generateConversionRateExcel(totalRate, valueRate, rate, inputRate);*/

        ConversionRate conversionRate = new ConversionRate()
                .withTotal(totalRate)
                .withValueRate(valueRate)
                .withRating(rate);
        List<ConversionRate> conversionRateList = new ArrayList<>();
        conversionRateList.add(conversionRate);

        ExcelUtils.writeToExcel(conversionRateList, fileName, this.getPath(), inputRate.getValueDescription());

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
