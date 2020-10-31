package fr.personnel.southsayerbackend.configuration.constant;

import com.adeo.spc.ws3.*;
import com.adeo.spc.ws3.storage.dto.GetOffersDTO;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Web Service Client
 * @version 1.0
 */
public final class WebServiceClient extends WebServiceGatewaySupport {
    /**
     * Get Offers
     *
     * @param simulationCode : simulationCode
     * @return {@link String}
     */
    public String getOffers(final String simulationCode) {
        final GetOffers request = new GetOffers();
        /*request.getTransactions().get(0).setCode(simulationCode);*/

        List<GetOffersDTO> transactions = request.getTransactions();
        GetOffersDTO getOffersDTO = new GetOffersDTO();
        getOffersDTO.setCode(simulationCode);
        getOffersDTO.setVersion(0);
        transactions.add(getOffersDTO);

        final GetOffers requestJaxb = new ObjectFactory().createGetOffers();
        requestJaxb.getTransactions().add(getOffersDTO);
        GetOffersResponse response = (GetOffersResponse) getWebServiceTemplate().marshalSendAndReceive(requestJaxb);

        return response.getOffers().get(0).getIdOffer();
    }

    /**
     * Associate Offer Transaction
     *
     * @param idOffer : idOffer from Get Offer
     * @return {@link String}
     */
    public String associateOfferTransaction(final String idOffer) {
        final AssociateOfferTransaction request = new AssociateOfferTransaction();
        request.getTransactions().get(0).setIdOffer(Long.parseLong(idOffer));

        final AssociateOfferTransaction requestJaxb = new ObjectFactory().createAssociateOfferTransaction();
        AssociateOfferTransactionResponse response = (AssociateOfferTransactionResponse) getWebServiceTemplate().marshalSendAndReceive(requestJaxb);

        return response.getOut();
    }
}
