package fr.personnel.southsayerbackend.service.rate;

import fr.personnel.southsayerdatabase.entity.rate.OAPDeliveryType;
import fr.personnel.southsayerdatabase.repository.rate.OAPDeliveryTypeRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


/**
 * @author Farouk KABOUCHE
 */
@Tag("OAPTypeRateServiceTests")
@DisplayName("OAP Type Rate Service Tests")
public class TypeRateTest {

    @InjectMocks
    private OAPTypeRateService typeRateService;

    @Mock
    private OAPDeliveryTypeRepository oapDeliveryTypeRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * {@link OAPTypeRateService#getAll()}
     */
    @Test
    @Tag("GetTypeRateServiceTests")
    @DisplayName("Get All OAP TR")
    public void getAll_TypeRate_Test() throws IOException {
        when(oapDeliveryTypeRepository.findAll()).thenReturn(createListTypeRates());

        Iterable<OAPDeliveryType> oapDeliveryTypes = typeRateService.getAll();

        verify(oapDeliveryTypeRepository, times(1)).findAll();
        assertNotNull(oapDeliveryTypes);
    }

    /**
     * {@link OAPTypeRateService#getByCodTypePrestation(String)}
     */
    @Test
    @Tag("GetTypeRateServiceTests")
    @DisplayName("Get OAP TR by code type prestation")
    public void get_TypeRate_ByCodTypePrestation_Test() throws IOException {
        when(oapDeliveryTypeRepository
                .findByCodTypePrestationLike(anyString()))
                .thenReturn(Optional.of(createListTypeRates()));
        String codTypePrestation = "51010";

        List<OAPDeliveryType> oapDeliveryTypes = typeRateService.getByCodTypePrestation(anyString());

        verify(oapDeliveryTypeRepository, times(1)).findByCodTypePrestationLike(anyString());
        assertThat(codTypePrestation).isEqualTo(oapDeliveryTypes.get(0).getCodTypePrestation());
        assertThat(codTypePrestation).isEqualTo(oapDeliveryTypes.get(1).getCodTypePrestation());
        assertNotNull(oapDeliveryTypes);
    }

    /**
     * {@link OAPTypeRateService#getByWordingDT(String)}
     */
    @Test
    @Tag("GetTypeRateServiceTests")
    @DisplayName("Get OAP TR by WordingDT")
    public void get_TypeRate_ByWordingDT_Test() throws IOException {
        when(oapDeliveryTypeRepository
                .findByLibTypePrestationLike(anyString()))
                .thenReturn(Optional.of(createListTypeRates()));
        String libTypePrestation = "Dépose";

        List<OAPDeliveryType> oapDeliveryTypes = typeRateService.getByWordingDT(anyString());

        verify(oapDeliveryTypeRepository, times(1)).findByLibTypePrestationLike(anyString());
        assertThat(libTypePrestation).isEqualTo(oapDeliveryTypes.get(0).getLibTypePrestation());
        assertThat(libTypePrestation).isEqualTo(oapDeliveryTypes.get(1).getLibTypePrestation());
        assertNotNull(oapDeliveryTypes);
    }

    /**
     * {@link OAPTypeRateService#getByIdOAP(Long)}
     */
    @Test
    @Tag("GetTypeRateServiceTests")
    @DisplayName("Get OAP TR by WordingDT")
    public void get_TypeRate_ByIdOAP_Test() throws IOException {
        when(oapDeliveryTypeRepository
                .findByIdOap(anyLong()))
                .thenReturn(Optional.of(createListTypeRates()));

        List<OAPDeliveryType> oapDeliveryTypes = typeRateService.getByIdOAP(anyLong());

        verify(oapDeliveryTypeRepository, times(1)).findByIdOap(anyLong());
        assertThat(Long.valueOf(1)).isEqualTo(oapDeliveryTypes.get(0).getIdOap());
        assertThat(Long.valueOf(2)).isEqualTo(oapDeliveryTypes.get(1).getIdOap());
        assertNotNull(oapDeliveryTypes);
    }


    /**
     * {@link OAPTypeRateService#save(List)}
     */
    @Test
    @Tag("AddAOAPTypeRateServiceTests")
    @DisplayName("Add OAP TR")
    public void add_OAPDeliveryType_Test() {
        when(oapDeliveryTypeRepository.save(any(OAPDeliveryType.class))).thenReturn(createTypeRate(1L));

        List<OAPDeliveryType> oapDeliveryTypes = typeRateService.save(createListTypeRates());

        verify(oapDeliveryTypeRepository, times(2)).save(any(OAPDeliveryType.class));
        assertNotNull(oapDeliveryTypes);
    }

    /**
     * {@link OAPTypeRateService#deleteByCodTypePrestation(List)}
     */
    @Test
    @Tag("DeleteActivityCodeServiceTests")
    @DisplayName("Delete OAP TR by CodTypePrestation")
    public void delete_OAPDeliveryType_ByCodTypePrestation_Test() {
        doNothing().when(oapDeliveryTypeRepository).deleteByCodTypePrestation(anyString());

        typeRateService.deleteByCodTypePrestation(Lists.newArrayList(anyString()));

        verify(oapDeliveryTypeRepository, times(1)).deleteByCodTypePrestation(anyString());
    }


    private OAPDeliveryType createTypeRate(Long id) {
        return new OAPDeliveryType("51010","Dépose",id);

    }

    private List<OAPDeliveryType> createListTypeRates() {
        return Lists.newArrayList(createTypeRate(1L), createTypeRate(2L));
    }


}
