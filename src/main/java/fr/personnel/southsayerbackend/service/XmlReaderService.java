package fr.personnel.southsayerbackend.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * Returns the value returned by the xpath for resultSet.
     * @param resultSet
     * @param xpath
     * @return {@link String}
     */
    public String readIntoXMLByXpath(String resultSet, String xpath) {
        String result = null;
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            resultSet = resultSet.replace("ns2:", "");
            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputXML = new InputSource(new StringReader(resultSet));

            result = xPath.evaluate(xpath,dBuilder.parse(inputXML));

        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Read into XML
     * @param xpath
     * @return {@link String}
     */
    public String readIntoXML(String xpath) {
        String searchValue = "";
        String nameDefaultFile = "src/main/resources/static/xml/XML_CONF.xml";
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
            /*String expression = "//*[@cpe=\"CPE.Settings.Session.CodeOffre\"]/@value";*/
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xPath.compile(xpath);
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
