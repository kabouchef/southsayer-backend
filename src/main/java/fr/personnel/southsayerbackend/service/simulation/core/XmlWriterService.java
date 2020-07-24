package fr.personnel.southsayerbackend.service.simulation.core;

import ch.qos.logback.classic.net.SimpleSocketServer;
import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.STATIC_DIRECTORY_FILES;
import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.XML_EXTENSION;

/**
 * @author Farouk KABOUCHE
 * Xml Writer Service
 * @version 1.0
 */
@Slf4j
@Service
@Data
@NoArgsConstructor
public class XmlWriterService {

    public void generateXML(String stringToXML,
                            String path,
                            String simulationCode) throws IOException {

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
