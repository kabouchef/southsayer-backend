package fr.personnel.southsayerbackend.utils.global;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.STATIC_DIRECTORY_FILES;

/**
 * @author Farouk KABOUCHE
 * Static Path Service
 * @version 1.0
 */
@Slf4j
@Service
@Data
public class StaticPathUtils {

    @Value("${ENVIRONMENT}")
    private String environment;
    @Value("${DATABASE_ENV_SCHEMA}")
    private String databaseEnvSchema;


    public String getPath(String extension, String target){
        return STATIC_DIRECTORY_FILES + "/" + environment + "/" + databaseEnvSchema +
                "/" + extension + "/" + target;
    }
}
