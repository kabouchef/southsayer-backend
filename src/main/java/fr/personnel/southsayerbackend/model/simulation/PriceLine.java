package fr.personnel.southsayerbackend.model.simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * @author Farouk KABOUCHE
 * Price Line
 * @version 1.0
 */
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class PriceLine {
    private String identifiant;
    private String detailPrestation;
    private String quantite;
    private String tarifUnitaire;
    private String tarifPrestation;
    private String typePrestation;
    private String prestationDe;
    private String tvaReduite;
    private String tvaInter;
    private String tvaNormale;
    private String code49;
    private String cod_typePrestation;
    private String tempPose;
    private String ordre;
    private double totalPriceHT;
    private double totalPriceTVAReduite;
    private double totalPriceTVAInter;
    private double totalPriceTVANormale;
}
