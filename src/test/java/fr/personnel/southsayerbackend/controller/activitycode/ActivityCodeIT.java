package fr.personnel.southsayerbackend.controller.activitycode;

import fr.personnel.southsayerbackend.service.activitycode.ActivityCodeService;
import fr.personnel.southsayerdatabase.entity.activitycode.ActivityCode;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = {ActivityCodeController.class, ActivityCodeService.class})
@ExtendWith(SpringExtension.class)
public class ActivityCodeIT {

    @Inject
    private MockMvc mockMvc;

    @MockBean
    private ActivityCodeService activityCodeService;

    @Autowired
    private ActivityCodeController activityCodeControllerUnderTest;

    @Test
    public void givenAUser_whenRequestIsMadeToAdd_thenASolutionSouldBeShown() throws Exception {
        when(activityCodeService.getByCodActivite(anyString())).thenReturn(createListActivityCode());

        final MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/byCodActivite")
                        .param("codActivite", "1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("id=\"solution\"")
                .contains(">5</span");
        verify(activityCodeService).getByCodActivite(anyString());

    }

    /*@Inject
    private MockMvc mockMvc;

    @MockBean
    private SolutionFormatter solutionFormatter;

    @MockBean
    private Calculator calculator;

    @Test
    public void givenACalculatorApp_whenRequestToAdd_thenSolutionIsShown() throws Exception {
        // GIVEN
        when(calculator.add(2, 3)).thenReturn(5);

        // WHEN
        final MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/calculator")
                        .param("leftArgument", "2")
                        .param("rightArgument", "3")
                        .param("calculationType", "ADDITION"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString())
                .contains("id=\"solution\"")
                .contains(">5</span");
        verify(calculator).add(2, 3);
        verify(solutionFormatter).format(5);
    }*/

    private ActivityCode createActivityCode(Long id) {
        return new ActivityCode("009630", "R09-Portail", id, "JARDIN", "OAP:016");

    }

    private List<ActivityCode> createListActivityCode() {
        return Lists.newArrayList(createActivityCode(1L), createActivityCode(2L));
    }
}
