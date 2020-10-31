package fr.personnel.southsayerbackend.model.simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * @author Farouk KABOUCHE
 * Update Value DTO
 * @version 1.0
 */
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class UpdateValueDTO {
    private XpathDefinition xpathDefinition;
    private String updatingValue;
}
