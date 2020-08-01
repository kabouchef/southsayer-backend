package fr.personnel.southsayerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Farouk KABOUCHE
 * API to manage OAP OracleDB LM
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = {"fr.personnel.southsayerbackend"})
@EnableJpaRepositories("fr.personnel.southsayerdatabase.repository")
@EntityScan("fr.personnel.southsayerdatabase.entity")
public class SouthsayerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SouthsayerBackendApplication.class, args);
    }

}
