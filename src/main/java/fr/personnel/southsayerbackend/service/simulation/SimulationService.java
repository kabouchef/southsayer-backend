package fr.personnel.southsayerbackend.service.simulation;

import fr.personnel.exceptions.handling.WebClientError.MethodNotAllowedException;
import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerbackend.model.simulation.ConversionRate;
import fr.personnel.southsayerbackend.model.simulation.InputRate;
import fr.personnel.southsayerbackend.model.simulation.PriceLine;
import fr.personnel.southsayerbackend.model.simulation.UpdateValueDTO;
import fr.personnel.southsayerbackend.service.simulation.core.ExcelConverterService;
import fr.personnel.southsayerbackend.service.simulation.core.ExtractFromDatabaseService;
import fr.personnel.southsayerbackend.service.simulation.core.StaticPathService;
import fr.personnel.southsayerbackend.utils.ExcelUtils;
import fr.personnel.southsayerbackend.utils.MathUtils;
import fr.personnel.southsayerbackend.utils.XmlUtils;
import fr.personnel.southsayerdatabase.entity.simulation.ConfigurationStorage;
import fr.personnel.southsayerdatabase.repository.simulation.ConfigurationStorageRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
import static fr.personnel.southsayerbackend.utils.ClobToStringUtils.stringToClob;
import static org.apache.commons.io.FileUtils.cleanDirectory;

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
    private final ExcelConverterService excelConverterService;
    private final XmlUtils xmlUtils;
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


    /**
     * Get Simulation Offer
     * @return {@link List<PriceLine>}
     */
    public List<PriceLine> getSimulationOffer(String simulationCode)
            throws IOException {


        List<PriceLine> tabPriceElement = null;
        simulationCode = simulationCode.replace(" ", "");
        cleanDirectory(new File(this.staticPathService.getPath(XML_EXTENSION, STATIC_DIRECTORY_SIMULATION)));

        try {
            // Create file XML_CONF.xml from data extracted
            this.xmlUtils.generateXML(
                    this.extractFromDatabaseService.getClobFromDatabase(simulationCode),
                    simulationCode);

            // Create Excel File PRICE_FROM_simulationCode.xls
            // File which present price lines of the simulation
            tabPriceElement = this.excelConverterService.generatePriceLinesExcel(simulationCode);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return tabPriceElement;
    }

    /**
     * Count All Simulation By ConfCategId (Like) ConfId (Like)
     * @return {@link int}
     */
    public int countAllByConfCategIdLikeConfIdLike(String confCategId, String confId) {
        return this.configurationStorageRepository.countByConfCategIdLikeAndConfIdLike(confCategId, confId);
    }

    /**
     * Conversion Rate By InputRate
     * @return {@link ConversionRate}
     */
    public ConversionRate conversionRateByInputRate(InputRate inputRate)
            throws IOException{

        double totalRate = this.countAllByConfCategIdLikeConfIdLike(
                        inputRate.getXpathDefinition().getIdOAP(),
                        inputRate.getXpathDefinition().getSimulationCode());

        double valueRate = this.extractFromDatabaseService.countSimulationsByValueByXpath(inputRate);

        double rate = MathUtils.calculatePercentage(valueRate, totalRate);

        ConversionRate conversionRate = new ConversionRate()
                .withTotal(totalRate)
                .withValueRate(valueRate)
                .withRating(rate);
        List<ConversionRate> conversionRateList = new ArrayList<>();
        conversionRateList.add(conversionRate);

        ExcelUtils.writeToExcel(conversionRateList, fileName, this.getPath(), inputRate.getValueDescription());

        return conversionRate;
    }

    /**
     * Update xmlConf By xpath
     * @return {@link List<String>}
     */
    public List<String> save(List<UpdateValueDTO> updateValueDTOS) {
        return updateValueDTOS
                        .stream()
                        .map(x -> {
                            Optional<ConfigurationStorage> oldConfigStorage =
                                    this.configurationStorageRepository
                                            .findByConfId(x.getXpathDefinition().getSimulationCode());

                            if (!oldConfigStorage.isPresent())
                                throw new MethodNotAllowedException(
                                        this.notFoundMessage.toString(x.getXpathDefinition().getSimulationCode()));

                            ConfigurationStorage c = oldConfigStorage.get();

                            // Get XMLCONF
                            ConfigurationStorage configurationStorage =
                                    new ConfigurationStorage()
                                            .withConfId(x.getXpathDefinition().getSimulationCode())
                                            .withUserId(c.getUserId())
                                            .withXmlConf(
                                                    stringToClob(
                                                            // Replace value in XMLCONF
                                                            this.extractFromDatabaseService
                                                                    .updateValueByXpath(
                                                                            x.getXpathDefinition(),
                                                                            x.getUpdatingValue())))
                                    .withDeleted(c.getDeleted())
                                    .withConfTypeId(c.getConfTypeId())
                                    .withConfCategId(c.getConfCategId())
                                    .withEditorId(c.getEditorId());

                            // Inject in database XMLCONF with the new value
                            this.configurationStorageRepository.save(configurationStorage);
                            return configurationStorage.getConfId();
                        })
                        .collect(Collectors.toList());
    }

    /**
     * Get SimCode By ConfCategId (Like)
     * @return {@link List<String>}
     */
    public List<String> getSimCodeByConfCategIdLike(String confCategId) {
        Optional<List<ConfigurationStorage>> configurationStorages =
                this.configurationStorageRepository.findByConfCategIdLike(confCategId);

        if (!configurationStorages.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(confCategId));

        return configurationStorages.get().stream()
                .filter(x -> x.getConfCategId().contains("OAP:0"))
                .map(ConfigurationStorage::getConfId)
                .collect(Collectors.toList());
    }


    /**
     * Convert Simulation To RT
     * @return {@link String}
     */
    public String convertSimulationToRT(String simulationCode) {
        return this.associateOfferTransaction(this.getOffers(simulationCode));
    }

    // TODO 6 - Post getOffers with simulationCode
    /**
     * Get Offers
     * @return {@link String}
     */
    private String getOffers(String simulationCode){
        return simulationCode;
    }

    // TODO 7 - Post associateOfferTransaction with idOffer from getOffers' result
    /**
     * Associate Offer Transaction
     * @return {@link String}
     */
    private String associateOfferTransaction(String idOffer){
        return idOffer;
    }
}
