package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.RequestServiceDTO;
import gestionPeluqueria.entities.composite.CompositeService;
import gestionPeluqueria.entities.composite.ServiceComponent;
import gestionPeluqueria.entities.composite.SimpleService;
import gestionPeluqueria.services.impl.HairServiceImpl;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceController.class)
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private HairServiceImpl mockHairService;

    private ServiceComponent service1;
    private ServiceComponent service2;
    private ServiceComponent service3;
    private List<ServiceComponent> servicesList;
    private List<ServiceComponent> emptyServicesList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service1 = new SimpleService("Corte Adulto", "Descripción Corte de Pelo Adulto",
                new BigDecimal("13.70"), new ArrayList<>(List.of(30)));
        service1.setId(1);
        service2 = new SimpleService("Tinte Adulto", "Descripción Tinte de Pelo Adulto",
                new BigDecimal("40.50"), new ArrayList<>(List.of(15, 30, 15)));
        service2.setId(2);
        service3 = new CompositeService("Corte+Tinte Adulto", "Descripción Corte+Tinte Adulto");
        service3.setId(3);
        ((CompositeService) service3).addService(service1);
        ((CompositeService) service3).addService(service2);

        servicesList = new ArrayList<>(List.of(service1, service2, service3));
        emptyServicesList = new ArrayList<>();
    }

    @Test
    void getAllServicesTest() throws Exception {
        // No hay servicios: RETURN 204 NO CONTENT
        when(mockHairService.findAll()).thenReturn(emptyServicesList);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/servicios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Hay servicios: RETURN 200 OK
        when(mockHairService.findAll()).thenReturn(servicesList);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/servicios")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    void getServiceByIdTest() throws Exception {
        // Caso No Válido: No existe el servicio (NOT FOUND)
        when(mockHairService.findById(6)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/servicios/6")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        // Caso Válido: Existe el servicio (OK)
        when(mockHairService.findById(service1.getId())).thenReturn(service1);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/servicios/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(service1.getName()));
    }

    @Test
    void createService() throws Exception {
        RequestServiceDTO requestSimple = new RequestServiceDTO();
        requestSimple.setName(service2.getName());
        requestSimple.setDescription(service2.getDescription());
        requestSimple.setPrice(service2.getPrice());
        requestSimple.setDuration(service2.getDuration());

        // Caso Válido: Se crea por primera vez un servicio (CREATED)
        when(mockHairService.createService(any(RequestServiceDTO.class))).thenReturn(service2);
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias/servicios")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(requestSimple)))
                .andExpect(status().isCreated());

        // Caso No Válido: Se crea un servicio existente (CONFLICT)
        when(mockHairService.createService(requestSimple)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias/servicios")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(requestSimple)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateHairdresser() throws Exception {
        ServiceComponent updatedService = new SimpleService();
        updatedService.setName("Corte Infantil");
        service1.setName(updatedService.getName());

        // Caso No Válido: Servicio No Existe (BAD REQUEST)
        when(mockHairService.updateService(6, updatedService)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/peluquerias/servicios/6")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(updatedService)))
                .andExpect(status().isBadRequest());

        // Caso Válido: Servicio Encontrado (OK)
        when(mockHairService.updateService(service1.getId(), updatedService)).thenReturn(service1);
        mockMvc.perform(MockMvcRequestBuilders.put("/peluquerias/servicios/" +service1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.asJsonString(updatedService)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Corte Infantil"));
    }

    @Test
    void deleteHairdresser() throws Exception {
        // Caso No Válido: Servicio No Existe (BAD REQUEST)
        when(mockHairService.findById(6)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.delete("/peluquerias/servicios/6")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        // Caso Válido: Servicio Existe (NO CONTENT)
        when(mockHairService.findById(service3.getId())).thenReturn(service3);
        mockMvc.perform(MockMvcRequestBuilders.delete("/peluquerias/servicios/" +service3.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
