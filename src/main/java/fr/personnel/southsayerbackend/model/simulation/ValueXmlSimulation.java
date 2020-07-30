package fr.personnel.southsayerbackend.model.simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * @author Farouk KABOUCHE
 * Xpath
 * @version 1.0
 */
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class ValueXmlSimulation {
    private String idOAP;
    private String simulationCode;
    private String value;
}
