package fr.personnel.southsayerbackend.configuration.constant;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBElement;
import java.util.List;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Web Service Client
 * @version 1.0
 */
//TODO 4 - Configure WebServiceClient
public final class WebServiceClient extends WebServiceGatewaySupport {

    /**
     * Get Prestation Type By Store
     *
     * @param id : id
     * @return {@link List < DomaineCompetenceLightDTO >}
     */
    public /*List<DomaineCompetenceLightDTO>*/ String getPrestationTypeByStore(final String id) {
        /*final GetTypePrestationByMagasin request = new GetTypePrestationByMagasin();
        request.setArg0(id);
        final JAXBElement<GetTypePrestationByMagasin> requestJaxb = new ObjectFactory().createGetTypePrestationByMagasin(request);

        final JAXBElement<GetTypePrestationByMagasinResponse> responseJaxb = (JAXBElement<GetTypePrestationByMagasinResponse>) getWebServiceTemplate().marshalSendAndReceive(requestJaxb);
        return responseJaxb.getValue().getReturn();*/

        return id;

    }
}
