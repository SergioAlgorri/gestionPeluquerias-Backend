package gestionPeluqueria.controllers;

import gestionPeluqueria.entities.Reward;
import gestionPeluqueria.services.impl.RewardServiceImpl;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private RewardServiceImpl mockRewardService;

    private Reward reward1;
    private Reward reward2;
    private List<Reward> rewardsList;
    private List<Reward> emptyRewardList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reward1 = new Reward("Descuento 5%", new BigDecimal("0.95"), 150,
                LocalDate.of(2025,10,10));
        reward1.setId(1);
        reward2 = new Reward("Descuento 15%", new BigDecimal("0.85"), 450,
                LocalDate.of(2025,6,8));
        reward2.setId(2);

        rewardsList = new ArrayList<>(List.of(reward1, reward2));
        emptyRewardList = new ArrayList<>();
    }

    @Test
    void getAllRewardsTest() throws Exception {
        // No hay recompensas: RETURN 204 NO CONTENT
        when(mockRewardService.findAll()).thenReturn(emptyRewardList);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/recompensas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Hay recompensas: RETURN 200 OK
        when(mockRewardService.findAll()).thenReturn(rewardsList);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/recompensas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    /*
    @Test
    void getRewardByIdTest() throws Exception {
        // Caso No Válido: No existe la recompensa (NOT FOUND)
        when(mockRewardService.findById(1)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/recompensas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        // Caso Válido: Existe la Recompensa (OK)
        when(mockRewardService.findById(1)).thenReturn(reward1);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/recompensas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(reward1.getName()));
    }
     */
    @Test
    void createReward() throws Exception {
        Reward reward3 = new Reward("Descuento 20%", new BigDecimal("0.80"), 650,
                LocalDate.of(2025, 4, 2));
        reward3.setId(3);

        // Caso Válido: Se crea por primera vez una recompensa (CREATED)
        when(mockRewardService.createReward(any(Reward.class))).thenReturn(reward3);
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias/recompensas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(reward3)))
                .andExpect(status().isCreated());

        // Caso No Válido: Se crea una recompensa existente (CONFLICT)
        when(mockRewardService.createReward(any(Reward.class))).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias/recompensas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(reward3)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateReward() throws Exception {
        Reward update = new Reward();
        update.setExpirationDate(LocalDate.of(2026,1,1));
        reward2.setExpirationDate(update.getExpirationDate());

        // Caso No Válido: Recompensa No Existe (BAD REQUEST)
        when(mockRewardService.updateReward(reward1.getId(), update)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/peluquerias/recompensas/" +reward1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(update)))
                .andExpect(status().isBadRequest());

        // Caso Válido: Recompensa Encontrada (OK)
        when(mockRewardService.updateReward(reward2.getId(), update)).thenReturn(reward2);
        mockMvc.perform(MockMvcRequestBuilders.put("/peluquerias/recompensas/" +reward2.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expirationDate").value("2026-01-01"));
    }

    @Test
    void deleteReward() throws Exception {
        // Caso No Válido: Recompensa No Existe (BAD REQUEST)
        when(mockRewardService.findById(reward1.getId())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.delete("/peluquerias/recompensas/" +reward1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        // Caso Válido: Recompensa Existe (NO CONTENT)
        when(mockRewardService.findById(reward2.getId())).thenReturn(reward2);
        mockMvc.perform(MockMvcRequestBuilders.delete("/peluquerias/recompensas/" +reward2.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}