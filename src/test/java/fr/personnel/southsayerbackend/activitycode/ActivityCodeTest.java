package fr.personnel.southsayerbackend.activitycode;

import com.google.common.collect.Lists;
import fr.personnel.southsayerbackend.service.activitycode.ActivityCodeService;
import fr.personnel.southsayerdatabase.entity.activitycode.ActivityCode;
import fr.personnel.southsayerdatabase.repository.activitycode.ActivityCodeRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


/**
 * @author Farouk KABOUCHE
 */
@Tag("ActivityCodeServiceTests")
@DisplayName("Activity Code Service Tests")
public class ActivityCodeTest {

    @InjectMocks
    private ActivityCodeService activityCodeService;

    @Mock
    private ActivityCodeRepository activityCodeRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * {@link ActivityCodeService#getByCodActivite(String)}
     */
    @Test
    @Tag("GetActivityCodeServiceTests")
    @DisplayName("Get OAP AC by activity code")
    public void get_ActivityCode_byActivityCode_Test() {
        when(activityCodeRepository.findByCodActivite(anyString())).thenReturn(Optional.of(createListActivityCode()));

        List<ActivityCode> activityCodes = activityCodeService.getByCodActivite(anyString());

        verify(activityCodeRepository, times(1)).findByCodActivite(anyString());
        assertThat(Long.valueOf(1)).isEqualTo(activityCodes.get(0).getIdRayon());
        assertThat(Long.valueOf(2)).isEqualTo(activityCodes.get(1).getIdRayon());
        assertNotNull(activityCodes);
    }

    /**
     * {@link ActivityCodeService#getByLib1(String)}
     */
    @Test
    @Tag("GetActivityCodeServiceTests")
    @DisplayName("Get OAP AC by lib1")
    public void get_ActivityCode_bylib1_Test() {
        when(activityCodeRepository.findByLib1Like(anyString())).thenReturn(Optional.of(createListActivityCode()));
        String lib1 = "R09-Portail";

        List<ActivityCode> activityCodes = activityCodeService.getByLib1(anyString());

        verify(activityCodeRepository, times(1)).findByLib1Like(anyString());
        assertThat(lib1).isEqualTo(activityCodes.get(0).getLib1());
        assertThat(lib1).isEqualTo(activityCodes.get(1).getLib1());
        assertNotNull(activityCodes);
    }

    /**
     * {@link ActivityCodeService#getByIdRayon(Long)}
     */
    @Test
    @Tag("GetActivityCodeServiceTests")
    @DisplayName("Get OAP AC by idRayon")
    public void get_ActivityCode_byIdRayon_Test() {
        when(activityCodeRepository.findByIdRayon(anyLong())).thenReturn(Optional.of(createListActivityCode()));

        List<ActivityCode> activityCodes = activityCodeService.getByIdRayon(anyLong());

        verify(activityCodeRepository, times(1)).findByIdRayon(anyLong());
        assertThat(Long.valueOf(1)).isEqualTo(activityCodes.get(0).getIdRayon());
        assertThat(Long.valueOf(2)).isEqualTo(activityCodes.get(1).getIdRayon());
        assertNotNull(activityCodes);
    }

    /**
     * {@link ActivityCodeService#getByRayon(String)}
     */
    @Test
    @Tag("GetActivityCodeServiceTests")
    @DisplayName("Get OAP AC by rayon")
    public void get_ActivityCode_byRayon_Test() {
        when(activityCodeRepository.findByRayon(anyString())).thenReturn(Optional.of(createListActivityCode()));
        String rayon = "JARDIN";

        List<ActivityCode> activityCodes = activityCodeService.getByRayon(anyString());

        verify(activityCodeRepository, times(1)).findByRayon(anyString());
        assertThat(rayon).isEqualTo(activityCodes.get(0).getRayon());
        assertThat(rayon).isEqualTo(activityCodes.get(1).getRayon());
        assertNotNull(activityCodes);
    }


    /**
     * {@link ActivityCodeService#getByIdOAP(String)}
     */
    @Test
    @Tag("GetActivityCodeServiceTests")
    @DisplayName("Get OAP AC by idOAP")
    public void get_ActivityCode_byIdOAP_Test() {
        when(activityCodeRepository.findByIdOap(anyString())).thenReturn(Optional.of(createListActivityCode()));
        String idOAP = "OAP:016";

        List<ActivityCode> activityCodes = activityCodeService.getByIdOAP(anyString());

        verify(activityCodeRepository, times(1)).findByIdOap(anyString());
        assertThat(idOAP).isEqualTo(activityCodes.get(0).getIdOap());
        assertThat(idOAP).isEqualTo(activityCodes.get(1).getIdOap());
        assertNotNull(activityCodes);
    }

    /**
     * {@link ActivityCodeService#getByIdOAP(String)}
     */
    @Test
    @Tag("AddActivityCodeServiceTests")
    @DisplayName("Add OAP AC")
    public void add_ActivityCode_Test() {
        when(activityCodeRepository.save(any(ActivityCode.class))).thenReturn(createActivityCode(1L));

        List<ActivityCode> activityCodes = activityCodeService.save(createListActivityCode());

        verify(activityCodeRepository, times(2)).save(any(ActivityCode.class));
        assertNotNull(activityCodes);
    }

    /**
     * {@link ActivityCodeService#getByIdOAP(String)}
     */
    @Test
    @Tag("DeleteActivityCodeServiceTests")
    @DisplayName("Delete OAP AC by activity code")
    public void delete_ActivityCode_ByActivityCode_Test() {
        doNothing().when(activityCodeRepository).deleteByCodActivite(anyString());

        activityCodeService.deleteByCodActivite(Lists.newArrayList(anyString()));

        verify(activityCodeRepository, times(1)).deleteByCodActivite(anyString());
    }


    private ActivityCode createActivityCode(Long id) {
        return new ActivityCode("009630", "R09-Portail", id, "JARDIN", "OAP:016");

    }

    private List<ActivityCode> createListActivityCode() {
        return Lists.newArrayList(createActivityCode(1L), createActivityCode(2L));
    }


}
