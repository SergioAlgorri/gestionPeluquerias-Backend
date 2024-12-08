package gestionPeluqueria.services.impl;

import gestionPeluqueria.entities.Reward;
import gestionPeluqueria.repositories.RewardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class RewardServiceImplTest {

    @Mock
    private RewardRepository mockRewardRepository;
    @InjectMocks
    private RewardServiceImpl rewardService;

    private Reward reward1;
    private Reward reward2;
    private Reward reward3;
    private List<Reward> rewardsList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rewardService = new RewardServiceImpl(mockRewardRepository);

        reward1 = new Reward("Descuento 5%", new BigDecimal("0.95"), 150,
                LocalDate.of(2025,10,10));
        reward1.setId(1);
        reward2 = new Reward("Descuento 15%", new BigDecimal("0.85"), 450,
                LocalDate.of(2025,6,8));
        reward2.setId(2);
        reward3 = new Reward("Descuento 20%", new BigDecimal("0.80"), 650,
                LocalDate.of(2025,4,2));
        reward3.setId(3);
        rewardsList = new ArrayList<>(List.of(reward1, reward2));

        when(mockRewardRepository.findById(1)).thenReturn(reward1);
        when(mockRewardRepository.findById(2)).thenReturn(reward2);
        when(mockRewardRepository.findById(3)).thenReturn(reward3);
    }

    @Test
    void findAll() {
        // Lista Vacía
        List<Reward> resultEmpty = rewardService.findAll();
        when(mockRewardRepository.findAll()).thenReturn(new ArrayList<>());
        verify(mockRewardRepository).findAll();
        assertTrue(resultEmpty.isEmpty(), "The list of rewards should be empty");

        // Lista Con Recompensas
        when(mockRewardRepository.findAll()).thenReturn(rewardsList);
        List<Reward> result = rewardService.findAll();

        verify(mockRewardRepository, times(2)).findAll();
        assertFalse(result.isEmpty(), "The list of rewards should NOT be empty");
        assertEquals(rewardsList.size(), result.size(),
                "The number of rewards should be equal to the expected value");
        assertEquals(reward1, result.get(0), "The reward should be equal to the expected value");
        assertEquals("Descuento 5%", result.get(0).getName(),
                "The name should be equal to the expected value");
        assertEquals(reward2, result.get(1), "The reward should be equal to the expected value");
        assertEquals("Descuento 15%", result.get(1).getName(),
                "The name should be equal to the expected value");
    }

    @Test
    void findById() {
        // Recompensa No Existente (ID No Válido)
        assertNull(rewardService.findById(6), "The reward should be null");

        // Servicio Existente (ID Válido)
        assertEquals(reward1, mockRewardRepository.findById(reward1.getId()),
                "The reward should be equal to the expected value");
        verify(mockRewardRepository).findById(1);
        assertEquals(reward2, mockRewardRepository.findById(reward2.getId()),
                "The reward should be equal to the expected value");
        verify(mockRewardRepository).findById(2);
    }

    @Test
    void createRewardTest() {
        // Crear Recompensa por primera vez
        when(mockRewardRepository.save(reward3)).thenReturn(reward3);
        Reward rewardCreated = rewardService.createReward(reward3);
        assertEquals(reward3, rewardCreated, "The reward should be equal");
        assertEquals(reward3.getName(), rewardCreated.getName(),
                "The name should be equal to the expected value");

        // Se vuelve a crear la misma recompensa
        when(mockRewardRepository.save(reward3)).thenReturn(null);
        assertNull(rewardService.createReward(reward3), "The reward should be null");
    }

    @Test
    void updateReward() {
        Reward updatedReward = new Reward();
        updatedReward.setName("Descuento 25%");
        updatedReward.setDiscountAmount(new BigDecimal("0.75"));

        // Se quiere actualizar recompensa no existente (No Válido)
        assertNull(rewardService.updateReward(6, updatedReward),
                "The result should be null (reward does not exit)");

        // Se actualiza un servicio existente (Válido)
        assertEquals("Descuento 5%", reward1.getName(),
                "The name should be equal to the expected value");
        assertEquals(new BigDecimal("0.95"), reward1.getDiscountAmount(),
                "The discount amount should be equal to the expected value");
        rewardService.updateReward(reward1.getId(), updatedReward);
        verify(mockRewardRepository).save(reward1);
        assertNotEquals("Descuento 5%", reward1.getName(), "The name should have changed");
        assertNotEquals(new BigDecimal("0.95"), reward1.getDiscountAmount(),
                "The discount amount should have changed");
        assertEquals(updatedReward.getName(), reward1.getName(),
                "The name should be equal to the expected value");
        assertEquals(updatedReward.getDiscountAmount(), reward1.getDiscountAmount(),
                "The discount amount should be equal to the expected value");
    }

    @Test
    void deleteReward() {
        // Se quiere eliminar un servicio no existente (No Válido)
        rewardService.deleteReward(6);
        verify(mockRewardRepository, never()).delete(any(Reward.class));

        // Se elimina un servicio existente (Válido)
        rewardService.deleteReward(reward3.getId());
        verify(mockRewardRepository).delete(reward3);
    }
}
