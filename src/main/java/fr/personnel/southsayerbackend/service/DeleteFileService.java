package fr.personnel.southsayerbackend.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author Farouk KABOUCHE
 *
 * Delete File Service
 */
@Slf4j
@Service
@NoArgsConstructor
public class DeleteFileService {

    /**
     * Delete Files By Path
     * @param directory : directory
     * @param target : target
     * @param simulationCode : simulationCode
     */
    public void DeleteFilesByPath(String directory, String target, String extension, String simulationCode){
        File dirTarget = new File(directory + target);
        File[] listFilesTarget = dirTarget.listFiles();

        log.info("*******************************");
        log.info("Deleting all files with the following extension : " + extension + " in : " + target);
        for(File file : listFilesTarget){
            if (!file.getName().equals(simulationCode + "." + extension)){
                log.info("File deleted : " + file.getName());
                file.delete();
            }
        }
        log.info("*******************************");
    }

    /**
     * Delete File By File
     * @param directory : directory
     */
    public void DeleteFileByFile(String directory){
        File dir = new File(directory);
        File[] listFiles = dir.listFiles();

        log.info("*******************************");
        for(File file : listFiles){
            log.info("Deleting "+file.getName());
            file.delete();
        }
        log.info("*******************************");
    }
}
