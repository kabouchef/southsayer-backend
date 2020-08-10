package fr.personnel.southsayerbackend.model.simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * @author Farouk KABOUCHE
 * Input Rate
 * @version 1.0
 */
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class InputRate {
    private XpathDefinition xpathDefinition;
    private String period;
    private String valueSearched;
    private String valueDescription;
}
