package fr.leroymerlin.oapcoreartyws.config;

import fr.personnel.southsayerbackend.configuration.constant.SoapConstant;
import fr.personnel.southsayerbackend.configuration.constant.WebServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


/**
 * @author Farouk KABOUCHE
 * <p>
 * Soap Connector
 */
@Configuration
@Slf4j
public class SoapConnector {

    @Value("${arty.storage.service.url}")
    private String urlWS;

    /**
     * Link package to web service call
     *
     * @return {@link Jaxb2Marshaller}
     */
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(SoapConstant.NAME_PACKAGE);
        return marshaller;
    }

    /**
     * Create Web Client in order to call Arty WS
     *
     * @return {@link WebServiceClient}
     */
    @Bean
    public WebServiceClient wsClient() {
        WebServiceClient client = new WebServiceClient();
        log.info("urlWS : " + urlWS);
        client.setDefaultUri(urlWS);
        client.setMarshaller(marshaller());
        client.setUnmarshaller(marshaller());
        return client;
    }
}
