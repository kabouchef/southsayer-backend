package fr.personnel.southsayerbackend.service.rate;

import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryRateDetails;
import fr.personnel.southsayerdatabase.repository.rate.OAPDeliveryRateDetailsRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


/**
 * @author Farouk KABOUCHE
 */
@Tag("OAPDeliveryRateServiceTests")
@DisplayName("OAP Delivery Rate Service Tests")
public class DeliveryRateTest {

    @InjectMocks
    private OAPDeliveryRateService deliveryRateService;

    @Mock
    private OAPDeliveryRateDetailsRepository deliveryRateDetailsRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    String id1 = "S_Pose_m2_Sol_souple_1";
    String id2 = "S_Pose_m2_Sol_souple_2";

    /**
     * {@link OAPDeliveryRateService#getAll()}
     */
    @Test
    @Tag("GetDeliveryRateServiceTests")
    @DisplayName("Get All OAP DR")
    public void getAll_DeliveryRate_Test() {
        when(deliveryRateDetailsRepository.findAll()).thenReturn(createListDeliveryRates());

        Iterable<OAPDeliveryRateDetails> oapDeliveryRateDetails = deliveryRateService.getAll();

        verify(deliveryRateDetailsRepository, times(1)).findAll();
        assertNotNull(oapDeliveryRateDetails);
    }

    /**
     * {@link OAPDeliveryRateService#getByIdentifiant(String)}
     */
    @Test
    @Tag("OAPDeliveryRateServiceTests")
    @DisplayName("Get OAP DR by identifiant")
    public void get_DeliveryRate_ByIdentifiant_Test() {
        when(deliveryRateDetailsRepository
                .findByIdentifiantLike(anyString()))
                .thenReturn(Optional.of(createListDeliveryRates()));

        List<OAPDeliveryRateDetails> oapDeliveryRateDetails = deliveryRateService.getByIdentifiant(anyString());

        verify(deliveryRateDetailsRepository, times(1)).findByIdentifiantLike(anyString());
        assertThat(id1).isEqualTo(oapDeliveryRateDetails.get(0).getIdentifiant());
        assertThat(id2).isEqualTo(oapDeliveryRateDetails.get(1).getIdentifiant());
        assertNotNull(oapDeliveryRateDetails);
    }

    /**
     * {@link OAPDeliveryRateService#getByLibelleId(String, String)}
     */
    @Test
    @Tag("OAPDeliveryRateServiceTests")
    @DisplayName("Get OAP DR by LibelleId")
    public void get_DeliveryRate_ByLibelleId_Test() {
        when(deliveryRateDetailsRepository
                .findByIdentifiantLikeAndLibelleIdentifiantLike(anyString(), anyString()))
                .thenReturn(Optional.of(createListDeliveryRates()));

        List<OAPDeliveryRateDetails> oapDeliveryRateDetails = deliveryRateService.getByLibelleId(anyString(), anyString());

        verify(deliveryRateDetailsRepository, times(1))
                .findByIdentifiantLikeAndLibelleIdentifiantLike(anyString(), anyString());
        assertNotNull(oapDeliveryRateDetails);
    }

    /**
     * {@link OAPDeliveryRateService#getByDesignation(String, String)}
     */
    @Test
    @Tag("OAPDeliveryRateServiceTests")
    @DisplayName("Get OAP DR by LibelleId")
    public void get_DeliveryRate_ByDesignation_Test() {
        when(deliveryRateDetailsRepository
                .findByIdentifiantLikeAndDesignationLike(anyString(), anyString()))
                .thenReturn(Optional.of(createListDeliveryRates()));

        List<OAPDeliveryRateDetails> oapDeliveryRateDetails =
                deliveryRateService.getByDesignation(anyString(), anyString());

        verify(deliveryRateDetailsRepository, times(1))
                .findByIdentifiantLikeAndDesignationLike(anyString(), anyString());
        assertNotNull(oapDeliveryRateDetails);
    }

    /**
     * {@link OAPDeliveryRateService#save(List)}
     */
    @Test
    @Tag("AddAOAPTypeRateServiceTests")
    @DisplayName("Add OAP DR")
    public void add_OAPDeliveryRate_Test() {
        when(deliveryRateDetailsRepository.save(any(OAPDeliveryRateDetails.class))).thenReturn(createDeliveryRate(id1));

        List<OAPDeliveryRateDetails> oapDeliveryTypes = deliveryRateService.save(createListDeliveryRates());

        verify(deliveryRateDetailsRepository, times(2)).save(any(OAPDeliveryRateDetails.class));
        assertNotNull(oapDeliveryTypes);
    }

    /**
     * {@link OAPDeliveryRateService#deleteByIdentifiant(List)}
     */
    @Test
    @Tag("DeleteActivityCodeServiceTests")
    @DisplayName("Delete OAP TR by Identifiant")
    public void delete_OAPDeliveryType_ByIdentifiant_Test() {
        doNothing().when(deliveryRateDetailsRepository).deleteByIdentifiant(anyString());

        deliveryRateService.deleteByIdentifiant(Lists.newArrayList(anyString()));

        verify(deliveryRateDetailsRepository, times(1)).deleteByIdentifiant(anyString());
    }


    private OAPDeliveryRateDetails createDeliveryRate(String id) {
        return new OAPDeliveryRateDetails(
                id, "Nombre de m2 de sol souple à poser", "61050",
                "S_Pose_m2_Sol_souple", (long) 7.50,(long) 8.70,"m2",
                (long) 0, null, (long) 0, (long) 0,(long) 0.00, (long) 0, (long) 0.00,
                (long) 0, (long) 0.00, "0", "TOUS", (long) 20180104, null,
                (long) 0, (long) 1, (long) 1, null, "ARTISAN_LM",
                "FR", (long) 26 );
    }

    private List<OAPDeliveryRateDetails> createListDeliveryRates() {
        return Lists.newArrayList(
                createDeliveryRate("S_Pose_m2_Sol_souple_1"),
                createDeliveryRate("S_Pose_m2_Sol_souple_2"));
    }


}
