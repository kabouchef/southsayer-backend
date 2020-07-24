package fr.personnel.southsayerbackend.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Farouk KABOUCHE
 * File Storage Properties
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "file")
@RefreshScope
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
