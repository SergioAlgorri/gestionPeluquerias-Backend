package gestionPeluqueria.services.impl;

import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.HairdresserCompany;
import gestionPeluqueria.repositories.HairdresserCompanyRepository;
import gestionPeluqueria.repositories.HairdresserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HairdresserServiceImplTest {

    @Mock
    private HairdresserRepository mockHairdresserRepository;
    @Mock
    private HairdresserCompanyRepository mockHairdresserCompanyRepository;
    @InjectMocks
    private HairdresserServiceImpl hairdresserService;

    private HairdresserCompany hairdresserCompany;
    private Hairdresser hairdresser1;
    private Hairdresser hairdresser2;
    private Hairdresser hairdresser3;
    private Hairdresser hairdresser4;
    private List<Hairdresser> hairdresserList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hairdresserService = new HairdresserServiceImpl(mockHairdresserRepository, mockHairdresserCompanyRepository);

        String nameCompany = "Peluquería UNICAN";
        hairdresserCompany = new HairdresserCompany(nameCompany);
        hairdresser1 = new Hairdresser(LocalTime.of(9,0,0),
                LocalTime.of(20,0,0), "C/Cadiz", "671718281");
        hairdresser1.setId(1);
        hairdresser2 = new Hairdresser(LocalTime.of(10,0,0),
                LocalTime.of(18,30,0), "C/Burgos", "611521011");
        hairdresser2.setId(2);
        hairdresser3 = new Hairdresser(LocalTime.of(8,0,0),
                LocalTime.of(21,0,0), "C/Alta", "600019121");
        hairdresser3.setId(3);
        hairdresserList = new ArrayList<>(List.of(hairdresser1, hairdresser2, hairdresser3));

        hairdresser4 = new Hairdresser(LocalTime.of(11,0,0),
                LocalTime.of(17,0,0), "C/Tetuán", "615358195");

        lenient().when(mockHairdresserRepository.findAll()).thenReturn(hairdresserList);

        lenient().when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser1);
        lenient().when(mockHairdresserRepository.findById(2)).thenReturn(hairdresser2);
        lenient().when(mockHairdresserRepository.findById(3)).thenReturn(hairdresser3);
        lenient().when(mockHairdresserRepository.findById(5)).thenReturn(null);

        lenient().when(mockHairdresserRepository.save(hairdresser4)).thenReturn(hairdresser4);
        lenient().when(mockHairdresserCompanyRepository.findByName(nameCompany)).thenReturn(hairdresserCompany);
        lenient().when(mockHairdresserRepository.save(hairdresser4)).thenReturn(null);
    }

    @Test
    void findAllTest() {
        List<Hairdresser> result = hairdresserService.findAll();

        verify(mockHairdresserRepository).findAll();
        assertFalse(result.isEmpty(), "The list of hairdressers should NOT be empty");
        assertEquals(hairdresserList.size(), result.size(),
                "There number of hairdressers should be equal to the expected value");
        assertEquals(hairdresser1, result.get(0), "The hairdresser should be equal to the expected value");
        assertEquals("C/Cadiz", result.get(0).getAddress(),
                "The address should be equal to the expected value");
        assertEquals(hairdresser2, result.get(1), "The hairdresser should be equal to the expected value");
        assertEquals("C/Burgos", result.get(1).getAddress(),
                "The address should be equal to the expected value");
        assertEquals(hairdresser3, result.get(2), "The hairdresser should be equal to the expected value");
        assertEquals("C/Alta", result.get(2).getAddress(),
                "The address should be equal to the expected value");
    }

    @Test
    void findByIdTest() {
        // ID No Válido
        assertNull(hairdresserService.findById(5));

        // ID Válido
        assertEquals(hairdresser1, hairdresserService.findById(hairdresser1.getId()),
                "The hairdresser should be equal to the expected value");
        assertEquals(hairdresser1.getId(), hairdresserService.findById(hairdresser1.getId()).getId(),
                "The id should be equal to the expected value");
        verify(mockHairdresserRepository, times(2)).findById(hairdresser1.getId());
        assertEquals(hairdresser2, hairdresserService.findById(hairdresser2.getId()),
                "The hairdresser should be equal to the expected value");
        assertEquals(hairdresser2.getId(), hairdresserService.findById(hairdresser2.getId()).getId(),
                "The id should be equal to the expected value");
        verify(mockHairdresserRepository, times(2)).findById(hairdresser2.getId());
        assertEquals(hairdresser3, hairdresserService.findById(hairdresser3.getId()),
                "The hairdresser should be equal to the expected value");
        assertEquals(hairdresser3.getId(), hairdresserService.findById(hairdresser3.getId()).getId(),
                "The id should be equal to the expected value");
        verify(mockHairdresserRepository, times(2)).findById(hairdresser3.getId());
    }

    @Test
    void createHairdresserTest() {
        Hairdresser hairdresserCreated = hairdresserService.createHairdresser(hairdresser4);
        verify(mockHairdresserRepository).save(hairdresser4);

        // Primera vez que se crea una peluquería (Válido)
        assertEquals(hairdresser4, hairdresserCreated, "The hairdressers should be equal");
        assertEquals(hairdresserCompany.getName(), hairdresserCreated.getCompany().getName(),
                 "The name of the company should be equal to the expected value");

        hairdresserList.add(hairdresserCreated);
        lenient().when(mockHairdresserRepository.findAll()).thenReturn(hairdresserList);

        // Se vuelve a crear la misma peluquería (No Válido)
        assertNull(hairdresserService.createHairdresser(hairdresser4), "The hairdresser should be null");
    }

    @Test
    void updateHairdresserTest() {
        Hairdresser updatedHairdresser = new Hairdresser();
        updatedHairdresser.setTelephone("123456789");

        // Se quiere actualizar no peluquería no existente (No Válido)
        assertNull(hairdresserService.updateHairdresser(5, updatedHairdresser),
                "The result should be null (hairdresser does not exit)");

        // Se actualiza una peluquería existente (Válido)
        String telephone = "671718281";
        assertEquals(telephone, hairdresser1.getTelephone(),
                "The telephone should be equal to the expected value");
        hairdresserService.updateHairdresser(hairdresser1.getId(), updatedHairdresser);
        verify(mockHairdresserRepository).save(hairdresser1);
        assertNotEquals(telephone, hairdresser1.getTelephone(), "The telephone should have changed");
        assertEquals(updatedHairdresser.getTelephone(), hairdresser1.getTelephone(),
                "The telephone should be equal to the expected value");
    }

    @Test
    void deleteHairdresserTest() {
        // Se quiere eliminar una peluquería no existente (No Válido)
        hairdresserService.deleteHairdresser(5);
        verify(mockHairdresserRepository, never()).delete(any(Hairdresser.class));

        // Se elimina una peluquería existente (Válido)
        hairdresserService.deleteHairdresser(hairdresser1.getId());
        verify(mockHairdresserRepository).delete(hairdresser1);
    }
}
