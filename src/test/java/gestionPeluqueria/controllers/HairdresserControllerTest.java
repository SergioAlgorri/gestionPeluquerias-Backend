package gestionPeluqueria.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.services.impl.HairdresserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HairdresserController.class)
public class HairdresserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HairdresserServiceImpl mockHairdresserService;

    private Hairdresser hairdresser1;
    private Hairdresser hairdresser2;
    private List<Hairdresser> hairdresserList;
    List<Hairdresser> emptyHairdresserList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hairdresser1 = new Hairdresser(LocalTime.of(9,0,0),
                LocalTime.of(20,0,0), "C/Cadiz", "671718281");
        hairdresser1.setId(1);
        hairdresser2 = new Hairdresser(LocalTime.of(10,0,0),
                LocalTime.of(18,30,0), "C/Burgos", "611521011");
        hairdresser2.setId(2);
        hairdresserList = new ArrayList<>(List.of(hairdresser1, hairdresser2));
        emptyHairdresserList = new ArrayList<>();
    }

    @Test
    void getAllHairdressersTest() throws Exception {
        // RETURN 204 NO CONTENT
        when(mockHairdresserService.findAll()).thenReturn(emptyHairdresserList);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // RETURN 200 OK
        when(mockHairdresserService.findAll()).thenReturn(hairdresserList);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getHairdresserByIdTest() throws Exception {
        when(mockHairdresserService.findById(1)).thenReturn(hairdresser1);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(hairdresser1.getAddress()));
    }

    @Test
    void createHairdresser() throws Exception {
        Hairdresser hairdresser = new Hairdresser(LocalTime.of(11,0,0),
                LocalTime.of(20,30,0), "C/Tetuán", "688811762");
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(hairdresser)))
                .andExpect(status().isCreated());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
