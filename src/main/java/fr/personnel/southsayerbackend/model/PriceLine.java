package fr.personnel.southsayerbackend.model;

import lombok.Data;

/**
 * @author Farouk KABOUCHE
 *
 * Price Line
 */
@Data
public class PriceLine {
    private String identifiant;
    private String detail_prestation;
    private String quantite;
    private String tarif_unitaire;
    private String tarif_prestation;
    private String type_prestation;
    private String prestation_de;
    private String tva_reduite;
    private String tva_inter;
    private String tva_normale;
    private String code_49;
    private String cod_type_prestation;
    private String temp_pose;
    private String ordre;
    private double totalPriceHT;
    private double totalPriceTVAReduite;
    private double totalPriceTVAInter;
    private double totalPriceTVANormale;
}
