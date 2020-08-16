package fr.personnel.southsayerbackend.utils.xml;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Farouk KABOUCHE
 * Xml Formatter Service
 * @version 1.0
 */
@Slf4j
@Service
public class XmlFormatterUtils {

    public String format(String input) {
        return prettyFormat(input, "2");
    }

    public static String prettyFormat(String input, String indent) {
        StringWriter stringWriter = new StringWriter();

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indent);
            transformer.transform(new StreamSource(new StringReader(input)), new StreamResult(stringWriter));

            return stringWriter.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
