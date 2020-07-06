package fr.personnel.southsayerbackend.service.simulation.core;

import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

/**
 * @author Farouk KABOUCHE
 *
 * Xml Reader Service
 */
@Slf4j
@Service
@Data
@NoArgsConstructor
public class XmlReaderService {

    @Value("${ENVIRONMENT}")
    private String environment;

    /**
     * Returns the value returned by the xpath for resultSet.
     * @param xmlString : xmlString
     * @param xpath : xpath
     * @return {@link String}
     */
    public String readIntoXMLByXpath(String xmlString, String xpath) {
        String result = null;
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            xmlString = xmlString.replace("ns2:", "");
            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputXML = new InputSource(new StringReader(xmlString));
            result = xPath.evaluate(xpath,dBuilder.parse(inputXML));

        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Read into XML
     * @param xpath : xpath
     * @return {@link String}
     */
    public String readIntoXML(String xpath) {
        String searchValue = "";
        String nameDefaultFile = RestConstantUtils.STATIC_DIRECTORY + "/" + RestConstantUtils.XML_EXTENSION +
                "/" + environment + "/" + "XML_CONF." + RestConstantUtils.XML_EXTENSION;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            /**
             * xmlDocument correspond au document xml parsé
             */
            try {
                Document xmlDocument = dBuilder.parse(nameDefaultFile);
            } catch (Exception e) {
                Logger.getLogger(e.getMessage());
            }
            /**
             * Recherche du resultat du xpath dans le xmlDocument
             */
            XPath xPath = XPathFactory.newInstance().newXPath();
            /**
             * @offerCode : Récupération du numéro de simulation de l'offre
             */
            searchValue = xPath.compile(xpath).evaluate(dBuilder.parse(nameDefaultFile), XPathConstants.STRING).toString();
            log.info("*******************************");
            log.info("Numéro de simulation : " + searchValue);
            log.info("*******************************");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchValue;
    }
}
