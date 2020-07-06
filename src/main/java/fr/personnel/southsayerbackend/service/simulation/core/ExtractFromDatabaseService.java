package fr.personnel.southsayerbackend.service.simulation.core;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.model.simulation.ValueXmlSimulation;
import fr.personnel.southsayerbackend.utils.ClobToStringUtils;
import fr.personnel.southsayerdatabase.entity.storage.ConfigurationStorage;
import fr.personnel.southsayerdatabase.repository.storage.ConfigurationStorageRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Results Extract Database
 */
@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class ExtractFromDatabaseService {

    private final ConfigurationStorageRepository configurationStorageRepository;
    private final XmlReaderService xmlReaderService;

    /**
     * Get simulation content
     *
     * @param simulationCode : simulation code
     * @return {@link String}
     */
    public String getClobFromDatabase(String simulationCode) {

        Optional<ConfigurationStorage> configStorage = this.configurationStorageRepository.findByConfId(simulationCode);

        if (!configStorage.isPresent())
            throw new NotFoundException("No offer match to the following simulation code: " + simulationCode);

        return new ClobToStringUtils().clobToString(configStorage.get().getXmlConf());
    }

    /**
     * Find Value which retrieves by xpath in each simulation
     *
     * @param idOAP        : Id OAP
     * @param sequenceChar : Characters Sequence searched
     * @return {@link List<ValueXmlSimulation>}
     */
    public List<ValueXmlSimulation> findSimulationBySequenceChar(String idOAP, String simualtionCode, String sequenceChar) {

        List<ConfigurationStorage> simulationsList = new ArrayList<>();

        try {
            simulationsList = this.configurationStorageRepository.findByConfCategIdLikeAndConfIdLike(idOAP, simualtionCode);

            if (simulationsList.isEmpty())
                throw new NotFoundException("No offer contains the following characters sequence: " + sequenceChar);

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return simulationsList.stream()
                .filter(x ->{
                    String xmlConf = new ClobToStringUtils().clobToString(x.getXmlConf());
                    return xmlConf.contains(sequenceChar);
                })
                .map(x -> {
                    ValueXmlSimulation valueXmlSimulation = new ValueXmlSimulation();
                    String simCode =
                            this.xmlReaderService
                                    .readIntoXMLByXpath(
                                            new ClobToStringUtils().clobToString(x.getXmlConf()),
                                            "//*[@cpe='CPE.Settings.Session.CodeOffre']/@value");
                    valueXmlSimulation.setSimulationCode(simCode);
                    valueXmlSimulation.setIdOAP(x.getConfCategId());
                    valueXmlSimulation.setValue(sequenceChar);
                    return valueXmlSimulation;
                })
                .collect(Collectors.toList());
    }

    /**
     * Find Value which retrieves by xpath in each simulation
     *
     * @param idOAP : Id OAP
     * @param xpath : Xpath to get the search value
     * @return {@link List<ValueXmlSimulation>}
     */
    public List<ValueXmlSimulation> findValueInSimulationByXpath(String idOAP, String simulationCode, String xpath) {

        List<ConfigurationStorage> simulationsList = new ArrayList<>();

        try {
            simulationsList = this.configurationStorageRepository.findByConfCategIdLikeAndConfIdLike(idOAP, simulationCode);

            if (simulationsList.isEmpty())
                throw new NotFoundException("No result matches your query with the following xpath : " + xpath);

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return simulationsList.stream()
                .map(x -> {
                    ValueXmlSimulation valueXmlSimulation = new ValueXmlSimulation();

                    String xmlConf = this.xmlReaderService.readIntoXMLByXpath(
                            new ClobToStringUtils().clobToString(x.getXmlConf()), xpath);

                    valueXmlSimulation.setSimulationCode(x.getConfId());
                    valueXmlSimulation.setIdOAP(x.getConfCategId());
                    valueXmlSimulation.setValue(xmlConf);
                    return valueXmlSimulation;
                })
                .collect(Collectors.toList());
    }

}
