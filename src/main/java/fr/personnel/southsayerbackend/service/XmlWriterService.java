package fr.personnel.southsayerbackend.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Farouk KABOUCHE
 *
 * Xml Writer Service
 */
@Slf4j
@Service
@Data
@NoArgsConstructor
public class XmlWriterService {

    public void generateXML(String stringToXML, String staticDir, String environment, String databaseEnvSchema, String simulationCode) {

        String extension = "xml";
        String target = extension + "/" + environment + "/" + databaseEnvSchema;
        String path = staticDir + target;
        String defaultFile = path + "/XML_CONF.xml";

        try {
            FileWriter fw = new FileWriter(new File(defaultFile));
            XmlFormatterService formatter = new XmlFormatterService();
            fw.write(formatter.format(stringToXML));

            fw.flush();
            fw.close();

            /**
             *  Get the file
             */
            String pathNewFile = path + "/" + simulationCode + "." + extension;
            File oldFile = new File(defaultFile);
            File newFile = new File(pathNewFile);
            oldFile.renameTo(newFile);

            /**
             * Check if the specified file exists or not
             */
            if (newFile.exists()) log.info("The following file : \"" + simulationCode + "." + extension + "\" has been created.");
            else log.info("The following file : \"" + simulationCode + "." + extension + "\" has not been created...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
