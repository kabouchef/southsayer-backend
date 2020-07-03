package fr.personnel.southsayerbackend.model;

import lombok.Data;

/**
 * @author Farouk KABOUCHE
 *
 * Xpath
 */
@Data
public class XpathDefinition {
    private String environment;
    private String schema;
    private String idOAP;
    private String xpath;
}
