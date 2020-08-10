package fr.personnel.southsayerbackend.model.simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * @author Farouk KABOUCHE
 * Conversion Rate
 * @version 1.0
 */
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRate {
    private double rating;
    private double valueRate;
    private double total;
}
