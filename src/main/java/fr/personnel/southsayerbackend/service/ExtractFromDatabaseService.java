package fr.personnel.southsayerbackend.service;

import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerdatabase.entity.ConfigurationStorage;
import fr.personnel.southsayerdatabase.repository.ConfigurationStorageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@AllArgsConstructor
public class ExtractFromDatabaseService {

    private final ConfigurationStorageRepository configurationStorageRepository;

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

        return configStorage.get().getXmlConf();
    }

    /**
     * Find Value which retrieves by xpath in each simulation
     *
     * @param idOAP        : Id OAP
     * @param sequenceChar : Characters Sequence searched
     * @return {@link List<String>}
     */
    public List<String> findSimulationBySequenceChar(String idOAP, String sequenceChar) {

        List<ConfigurationStorage> simulationsList = new ArrayList<>();

        try {
            simulationsList = this.configurationStorageRepository.findByConfCategIdAndXmlConfLike(idOAP, sequenceChar);

            if (simulationsList.isEmpty())
                throw new NotFoundException("No offer contains the following characters sequence: " + sequenceChar);

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return simulationsList.stream().map(ConfigurationStorage::getConfId).collect(Collectors.toList());
    }

    /**
     * Find Value which retrieves by xpath in each simulation
     *
     * @param idOAP : Id OAP
     * @param xpath : Xpath to get the search value
     * @return {@link Map<String, String>}
     */
    public Map<String, String> findValueInSimulationByXpath(String idOAP, String xpath) {

        List<ConfigurationStorage> simulationsList = new ArrayList<>();

        try {
            simulationsList = this.configurationStorageRepository.findByConfCategId(idOAP);

            if (simulationsList.isEmpty())
                throw new NotFoundException("No result matches your query with the following xpath : " + xpath);

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return simulationsList.stream().collect(Collectors.toMap(ConfigurationStorage::getConfId, ConfigurationStorage::getXmlConf));
    }

}
