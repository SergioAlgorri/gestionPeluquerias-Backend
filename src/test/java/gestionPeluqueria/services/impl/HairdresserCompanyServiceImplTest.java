package gestionPeluqueria.services.impl;

import gestionPeluqueria.entities.HairdresserCompany;
import gestionPeluqueria.repositories.HairdresserCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HairdresserCompanyServiceImplTest {

    @Mock
    private HairdresserCompanyRepository mockHairdresserCompanyRepository;
    @InjectMocks
    private HairdresserCompanyServiceImpl hairdresserCompanyService;

    private HairdresserCompany hairdresserCompany;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hairdresserCompanyService = new HairdresserCompanyServiceImpl(mockHairdresserCompanyRepository);

        hairdresserCompany = new HairdresserCompany("VALIDO");

        when(mockHairdresserCompanyRepository.findByName("NO VALIDO")).thenReturn(null);
        when(mockHairdresserCompanyRepository.findByName("VALIDO")).thenReturn(hairdresserCompany);
    }

    @Test
    void findByNameTest() {
        // Compañía no existente con ese nombre (No Válido)
        assertNull(hairdresserCompanyService.findByName("NO VALIDO"));
        verify(mockHairdresserCompanyRepository).findByName("NO VALIDO");

        // Compañía existente con ese nombre (Válido)
        HairdresserCompany hairdresserCompanyFound = hairdresserCompanyService.findByName("VALIDO");
        assertEquals(hairdresserCompany.getName(), hairdresserCompanyFound.getName(),
                "The name should be equal to the expected value");
        verify(mockHairdresserCompanyRepository).findByName("VALIDO");
    }
}
