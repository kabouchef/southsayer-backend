package fr.personnel.southsayerbackend.utils;

import fr.personnel.southsayerbackend.service.simulation.core.StaticPathService;
import fr.personnel.southsayerbackend.service.simulation.core.XmlFormatterService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.*;
import static org.apache.commons.io.FileUtils.cleanDirectory;

/**
 * @author Farouk KABOUCHE
 * Xml Reader Service
 * @version 1.0
 */
@Slf4j
@Service
@Data
public class XmlUtils {

    @Value("${ENVIRONMENT}")
    private String environment;

    private final StaticPathService staticPathService;

    /**
     * Returns the value returned by the xpath for resultSet.
     *
     * @param xmlString : xmlString
     * @param xpath     : xpath
     * @return {@link String}
     */
    public String readIntoXMLByXpath(String xmlString, String xpath) {
        String result = null;
        try {
            DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
            df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            DocumentBuilder dBuilder = df.newDocumentBuilder();

            xmlString = xmlString.replace("ns2:", "");
            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputXML = new InputSource(new StringReader(xmlString));
            result = xPath.evaluate(xpath, dBuilder.parse(inputXML));

        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Replace Value Into XML By Xpath
     * @param xmlString : xmlString
     * @param xpath : xpath
     * @param updatingValue : updatingValue
     * @return {@link String}
     */
    public String replaceValueIntoXMLByXpath(String xmlString, String xpath, String updatingValue) {
        try {
        // 0- Prepare Output
        /*String path = this.staticPathService.getPath(XML_EXTENSION, STATIC_DIRECTORY_OUTPUT_FILE);
        String target = "Output.xml";
        cleanDirectory(new File(path));*/

            StringWriter sw = new StringWriter();

            // 1- Build the doc from the XML file
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));
            // 2- Locate the node(s) with xpath
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xPath.evaluate(xpath, doc, XPathConstants.NODESET);
            // 3- Make the change on the selected nodes
            for (int idx = 0; idx < nodes.getLength(); idx++) {
                nodes.item(idx).setTextContent(updatingValue);
            }
            // 4- Save the result to a new XML doc
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();

        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String : ", ex);
        }
    }

    /**
     * Read into XML
     *
     * @param xpath : xpath
     * @return {@link String}
     */
    public String readIntoXML(String xpath) {
        String searchValue = "";
        String nameDefaultFile = STATIC_DIRECTORY_FILES + "/" + XML_EXTENSION + "/" + environment + "/" + "XML_CONF." + XML_EXTENSION;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            /**
             * xmlDocument correspond au document xml parsé
             */
            dBuilder.parse(nameDefaultFile);

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

    public void generateXML(String stringToXML, String simulationCode) throws IOException {

        String path = this.staticPathService.getPath(XML_EXTENSION, STATIC_DIRECTORY_SIMULATION);

        String defaultFile = path + "/XML_CONF.xml";

        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(defaultFile));
            XmlFormatterService formatter = new XmlFormatterService();
            fw.write(formatter.format(stringToXML));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fw.flush();
            fw.close();
            /**
             *  Get the file
             */
            String pathNewFile = path + "/" + simulationCode + "." + XML_EXTENSION;
            File oldFile = new File(defaultFile);
            File newFile = new File(pathNewFile);
            boolean renamingFile = oldFile.renameTo(newFile);

            /**
             * Check if the specified file exists or not
             */
            log.info("*******************************");
            if (renamingFile){
                log.info("\"" + simulationCode + "." + XML_EXTENSION + "\" has been created in :");
                log.info(path);
            }
            else
                log.info("\"" + simulationCode + "." + XML_EXTENSION + "\" has not been created...");
            log.info("*******************************");
        }
    }
}
