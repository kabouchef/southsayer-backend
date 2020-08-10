package fr.personnel.southsayerbackend.service.simulation.core;

import fr.personnel.exceptions.handling.WebClientError.MethodNotAllowedException;
import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerbackend.model.simulation.ValueXmlSimulation;
import fr.personnel.southsayerbackend.model.simulation.XpathDefinition;
import fr.personnel.southsayerbackend.model.simulation.ConversionRate;
import fr.personnel.southsayerbackend.model.simulation.InputRate;
import fr.personnel.southsayerbackend.utils.XmlUtils;
import fr.personnel.southsayerdatabase.entity.simulation.ConfigurationStorage;
import fr.personnel.southsayerdatabase.repository.simulation.ConfigurationStorageRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fr.personnel.southsayerbackend.utils.ClobToStringUtils.clobToString;

/**
 * @author Farouk KABOUCHE
 * Results Extract Database
 * @version 1.0
 * @see TotalPricesService
 * @see ExcelConverterService
 */
@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class ExtractFromDatabaseService {

    private final ConfigurationStorageRepository configurationStorageRepository;
    private final XmlUtils xmlUtils;
    private final NotFoundMessage notFoundMessage;

    private List<ConfigurationStorage> simulationsList = new ArrayList<>();

    /**
     * Get simulation content
     *
     * @param simulationCode : simulation code
     * @return {@link String}
     */
    public String getClobFromDatabase(String simulationCode) {

        Optional<ConfigurationStorage> configStorage = this.configurationStorageRepository.findByConfId(simulationCode);

        if (!configStorage.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(simulationCode));

        if (!configStorage.get().getConfCategId().contains("OAP:0"))
            throw new MethodNotAllowedException(this.notFoundMessage.toString(simulationCode));

        return clobToString(configStorage.get().getXmlConf());
    }

    /**
     * Find Value which retrieves by xpath in each simulation
     *
     * @param inputRate : inputRate
     * @return {@link List<ValueXmlSimulation>}
     */
    public List<ValueXmlSimulation> findSimulationBySequenceChar(InputRate inputRate) {

        return this.getSimulationList(inputRate.getXpathDefinition())
                .stream()
                .filter(x ->
                        clobToString(x.getXmlConf()).contains(inputRate.getValueSearched()))
                .map(x -> new ValueXmlSimulation()
                        .withSimulationCode(this.xmlUtils
                        .readIntoXMLByXpath(
                                clobToString(x.getXmlConf()),
                                inputRate.getXpathDefinition().getXpath()))
                        .withIdOAP(x.getConfCategId())
                        .withValue(inputRate.getValueSearched())).collect(Collectors.toList());
    }

    /**
     * Find Value which retrieves by xpath in each simulation
     *
     * @param xpathDefinition : xpathDefinition
     * @return {@link List<ValueXmlSimulation>}
     */
    public List<ValueXmlSimulation> findValueInSimulationByXpath(XpathDefinition xpathDefinition) {
        return this.getSimulationList(xpathDefinition)
                .stream()
                .map(x -> new ValueXmlSimulation()
                        .withSimulationCode(x.getConfId())
                        .withIdOAP(x.getConfCategId())
                        .withValue(this.xmlUtils.readIntoXMLByXpath(
                                clobToString(x.getXmlConf()),xpathDefinition.getXpath())))
                .collect(Collectors.toList());
    }

    /**
     * Update Value By Xpath and retrieves String
     *
     * @param xpathDefinition : xpathDefinition
     * @param updatingValue : updatingValue
     * @return {@link String}
     */
    public String updateValueByXpath(XpathDefinition xpathDefinition, String updatingValue) {
        return this.xmlUtils.replaceValueIntoXMLByXpath(
                this.getClobFromDatabase(xpathDefinition.getSimulationCode()), xpathDefinition.getXpath(),
                updatingValue);
    }

    /**
     * Get rate which retrieves value searched by xpath
     *
     * @param inputRate : inputRate
     * @return {@link ConversionRate}
     */
    public double countSimulationsByValueByXpath(InputRate inputRate) {
        return this.getSimulationList(inputRate.getXpathDefinition())
                .stream()
                .filter(x -> {
                    String value = this.xmlUtils.readIntoXMLByXpath(
                            clobToString(x.getXmlConf()),
                            inputRate.getXpathDefinition().getXpath());
                    return value.equals(inputRate.getValueSearched());})
                .count();
    }

    private List<ConfigurationStorage> getSimulationList(XpathDefinition xpathDefinition) {
        return Optional.ofNullable(this.configurationStorageRepository
                .findByConfCategIdLikeAndConfIdLike(
                        xpathDefinition.getIdOAP(),
                        xpathDefinition.getSimulationCode()))
                .orElseThrow(() -> new NotFoundException(
                        this.notFoundMessage.toString(xpathDefinition.getIdOAP(),xpathDefinition.getSimulationCode())));
    }
}
