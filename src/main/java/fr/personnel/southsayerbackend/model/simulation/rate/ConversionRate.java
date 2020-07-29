package fr.personnel.southsayerbackend.model.simulation.rate;

import jdk.jfr.Percentage;
import lombok.Data;

/**
 * @author Farouk KABOUCHE
 * Conversion Rate
 * @version 1.0
 */
@Data
public class ConversionRate {
    private double rating;
    private double valueRate;
    private double total;
}
