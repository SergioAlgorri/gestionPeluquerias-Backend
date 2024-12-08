package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.RequestServiceDTO;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.composite.CompositeService;
import gestionPeluqueria.entities.composite.ServiceComponent;
import gestionPeluqueria.entities.composite.SimpleService;
import gestionPeluqueria.repositories.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HairServiceImplTest {

    @Mock
    private ServiceRepository mockServiceRepository;
    @InjectMocks
    private HairServiceImpl hairService;

    private ServiceComponent service1;
    private ServiceComponent service2;
    private ServiceComponent service3;
    private ServiceComponent service4;
    private List<ServiceComponent> servicesList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hairService = new HairServiceImpl(mockServiceRepository);

        service1 = new SimpleService("Corte Adulto", "Descripción Corte de Pelo Adulto",
                new BigDecimal("13.70"), new ArrayList<>(List.of(30)));
        service1.setId(1);
        service2 = new SimpleService("Tinte Adulto", "Descripción Tinte de Pelo Adulto",
                new BigDecimal("40.50"), new ArrayList<>(List.of(15, 30, 15)));
        service2.setId(2);
        service3 = new SimpleService("Peinado Adulto", "Descripción Peinado Adulto",
                new BigDecimal("40.50"), new ArrayList<>(List.of(15, 30, 15)));
        service3.setId(3);
        service4 = new CompositeService("Corte+Tinte Adulto", "Descripción Corte+Tinte Adulto");
        service4.setId(4);
        servicesList = new ArrayList<>(List.of(service1, service2));

        when(mockServiceRepository.findById(1)).thenReturn(service1);
        when(mockServiceRepository.findById(2)).thenReturn(service2);
        when(mockServiceRepository.findById(3)).thenReturn(service3);
    }

    @Test
    void findAll() {
        // Lista Vacía
        List<ServiceComponent> resultEmpty = hairService.findAll();
        when(mockServiceRepository.findAll()).thenReturn(new ArrayList<>());
        verify(mockServiceRepository).findAll();
        assertTrue(resultEmpty.isEmpty(), "The list of services should be empty");

        // Lista Con Servicios
        when(mockServiceRepository.findAll()).thenReturn(servicesList);
        List<ServiceComponent> result = hairService.findAll();

        verify(mockServiceRepository, times(2)).findAll();
        assertFalse(result.isEmpty(), "The list of services should NOT be empty");
        assertEquals(servicesList.size(), result.size(),
                "The number of services should be equal to the expected value");
        assertEquals(service1, result.get(0), "The service should be equal to the expected value");
        assertEquals("Corte Adulto", result.get(0).getName(),
                "The name should be equal to the expected value");
        assertEquals(service2, result.get(1), "The service should be equal to the expected value");
        assertEquals("Tinte Adulto", result.get(1).getName(),
                "The name should be equal to the expected value");
    }

    @Test
    void findById() {
        // Servicio No Existente (ID No Válido)
        assertNull(hairService.findById(6), "The service should be null");

        // Servicio Existente (ID Válido)
        assertEquals(service1, mockServiceRepository.findById(service1.getId()),
                "The service should be equal to the expected value");
        verify(mockServiceRepository).findById(1);
        assertEquals(service2, mockServiceRepository.findById(service2.getId()),
                "The service should be equal to the expected value");
        verify(mockServiceRepository).findById(2);
    }

    @Test
    void createServiceTest() {
        // Crear Servicio Simple
        RequestServiceDTO requestSimple = new RequestServiceDTO();
        requestSimple.setName(service3.getName());
        requestSimple.setDescription(service3.getDescription());
        requestSimple.setPrice(service3.getPrice());
        requestSimple.setDuration(service3.getDuration());

        when(mockServiceRepository.save(any())).thenReturn(service3);
        when(mockServiceRepository.findAll()).thenReturn(servicesList);
        ServiceComponent serviceCreated = hairService.createService(requestSimple);
        assertEquals(service3, serviceCreated, "The service should be equal");
        assertEquals(service3.getName(), serviceCreated.getName(),
                "The name should be equal to the expected value");

        // Crear Servicio Compuesto
        RequestServiceDTO requestComposite = new RequestServiceDTO();
        requestComposite.setName(service4.getName());
        requestComposite.setDescription(service4.getDescription());
        requestComposite.setServices(new ArrayList<>(List.of(1L, 2L)));

        when(mockServiceRepository.save(any())).thenReturn(service4);
        ServiceComponent serviceCompositeCreated = hairService.createService(requestComposite);
        assertEquals(service4, serviceCompositeCreated, "The service should be equal");
        assertEquals(service4.getName(), serviceCompositeCreated.getName(),
                "The name should be equal to the expected value");

        // Se vuelve a crear el mismo servicio
        when(mockServiceRepository.save(any())).thenReturn(null);
        assertNull(hairService.createService(requestSimple), "The service should be null");
    }

    @Test
    void updateService() {
        ServiceComponent updatedService = new SimpleService();
        updatedService.setName("Corte Infantil");

        // Se quiere actualizar servicio no existente (No Válido)
        assertNull(hairService.updateService(6, updatedService),
                "The result should be null (service does not exit)");

        // Se actualiza un servicio existente (Válido)
        String name = "Corte Adulto";
        assertEquals(name, service1.getName(), "The name should be equal to the expected value");
        hairService.updateService(service1.getId(), updatedService);
        verify(mockServiceRepository).save(service1);
        assertNotEquals(name, service1.getName(), "The name should have changed");
        assertEquals(updatedService.getName(), service1.getName(),
                "The name should be equal to the expected value");
    }

    @Test
    void deleteService() {
        // Se quiere eliminar un servicio no existente (No Válido)
        hairService.deleteService(6);
        verify(mockServiceRepository, never()).delete(any(ServiceComponent.class));

        // Se elimina un servicio existente (Válido)
        hairService.deleteService(service3.getId());
        verify(mockServiceRepository).delete(service3);
    }
}
