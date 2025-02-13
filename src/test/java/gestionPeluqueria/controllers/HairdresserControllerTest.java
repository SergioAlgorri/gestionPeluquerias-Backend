package gestionPeluqueria.controllers;

import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.HairdresserCompany;
import gestionPeluqueria.services.impl.HairdresserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
@WebMvcTest(HairdresserController.class)
public class HairdresserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
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
        hairdresser1.setCompany(new HairdresserCompany());
        hairdresser2 = new Hairdresser(LocalTime.of(10,0,0),
                LocalTime.of(18,30,0), "C/Burgos", "611521011");
        hairdresser2.setId(2);
        hairdresser2.setCompany(new HairdresserCompany());
        hairdresserList = new ArrayList<>(List.of(hairdresser1, hairdresser2));
        emptyHairdresserList = new ArrayList<>();
    }

    @Test
    void getAllHairdressersTest() throws Exception {
        // No hay peluquerías: RETURN 204 NO CONTENT
        when(mockHairdresserService.findAll()).thenReturn(emptyHairdresserList);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Hay peluquerías: RETURN 200 OK
        when(mockHairdresserService.findAll()).thenReturn(hairdresserList);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    void getHairdresserByIdTest() throws Exception {
        // Caso No Válido: No existe la peluquería (NOT FOUND)
        when(mockHairdresserService.findById(1)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        // Caso Válido: Existe la Peluquería (OK)
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
        hairdresser.setCompany(new HairdresserCompany());

        // Caso Válido: Se crea por primera vez una peluquería (CREATED)
        when(mockHairdresserService.createHairdresser(hairdresser)).thenReturn(hairdresser);
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtils.asJsonString(hairdresser)))
                .andExpect(status().isCreated());

        // Caso No Válido: Se crea una peluquería existente (CONFLICT)
        when(mockHairdresserService.createHairdresser(hairdresser1)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtils.asJsonString(hairdresser1)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateHairdresser() throws Exception {
        Hairdresser update = new Hairdresser();
        update.setTelephone("123456789");
        hairdresser2.setTelephone(update.getTelephone());

        // Caso No Válido: Peluquería No Existe (BAD REQUEST)
        when(mockHairdresserService.updateHairdresser(hairdresser1.getId(), update)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/peluquerias/" +hairdresser1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(update)))
                .andExpect(status().isBadRequest());

        // Caso Válido: Peluquería Encontrada (OK)
        when(mockHairdresserService.updateHairdresser(hairdresser2.getId(), update)).thenReturn(hairdresser2);
        mockMvc.perform(MockMvcRequestBuilders.put("/peluquerias/" +hairdresser2.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtils.asJsonString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telephone").value("123456789"));
    }

    @Test
    void deleteHairdresser() throws Exception {
        // Caso No Válido: Peluquería No Existe (BAD REQUEST)
        when(mockHairdresserService.findById(hairdresser1.getId())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.delete("/peluquerias/" +hairdresser1.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        // Caso Válido: Peluquería Existe (NO CONTENT)
        when(mockHairdresserService.findById(hairdresser2.getId())).thenReturn(hairdresser2);
        mockMvc.perform(MockMvcRequestBuilders.delete("/peluquerias/" +hairdresser2.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
*/