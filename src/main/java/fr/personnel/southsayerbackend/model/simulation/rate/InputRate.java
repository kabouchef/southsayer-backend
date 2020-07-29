package fr.personnel.southsayerbackend.model.simulation.rate;

import fr.personnel.southsayerbackend.model.simulation.XpathDefinition;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Farouk KABOUCHE
 * Conversion Rate
 * @version 1.0
 */
@Data
public class InputRate {
    private XpathDefinition xpathDefinition;
    private String valueSearched;
}
