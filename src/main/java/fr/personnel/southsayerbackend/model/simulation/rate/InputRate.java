package fr.personnel.southsayerbackend.model.simulation.rate;

import fr.personnel.southsayerbackend.model.simulation.XpathDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Farouk KABOUCHE
 * Conversion Rate
 * @version 1.0
 */
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class InputRate {
    private XpathDefinition xpathDefinition;
    private String valueSearched;
}
