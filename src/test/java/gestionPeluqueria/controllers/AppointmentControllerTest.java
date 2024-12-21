package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.RequestAppointmentDTO;
import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.composite.ServiceComponent;
import gestionPeluqueria.entities.composite.SimpleService;
import gestionPeluqueria.services.impl.AppointmentServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AppointmentServiceImpl mockAppointmentService;

    private Hairdresser hairdresser;
    private Employee employee;
    private User client;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear peluquería
        hairdresser = new Hairdresser();
        hairdresser.setId(1);

        // Crear empleado
        employee = new Employee();
        employee.setId(2L);
        hairdresser.getEmployees().add(employee);

        // Crear cliente
        client = new Client();
        client.setId(3);

        // Crear cita
        ServiceComponent service = new SimpleService("Corte", "Corte básico",
                new BigDecimal("15.00"), List.of(30));
        service.setId(4);
        appointment = new Appointment(LocalDateTime.of(2024, 12, 20, 10, 0),
                "Corte de pelo", client, employee, service,null, hairdresser);
        appointment.setId(5);
        hairdresser.getAppointments().add(appointment);
    }

    @Test
    void findAllTest() throws Exception {
        // 400 BAD REQUEST. Peluquería y/o empleado no existe
        when(mockAppointmentService.findAll(2, null, null)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/"+2+"/citas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockAppointmentService.findAll(1, 4L, null)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/"+hairdresser.getId()+"/citas")
                        .param("idEmployee", "4L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // 204 NO CONTENT. Peluquería y empleado existen pero no hay citas activas
        when(mockAppointmentService.findAll(1, 2L, null)).thenReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/"+hairdresser.getId()+"/citas")
                        .param("idEmployee", String.valueOf(employee.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 200 OK
        when(mockAppointmentService.findAll(1, null, null))
                .thenReturn(hairdresser.getAppointments());
        mockMvc.perform(MockMvcRequestBuilders.get("/peluquerias/"+hairdresser.getId()+"/citas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    void createAppointmentTest() throws Exception {
        // 201 CREATED
        RequestAppointmentDTO request = new RequestAppointmentDTO();
        when(mockAppointmentService.createAppointment(hairdresser.getId(), request)).thenReturn(appointment);
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias/"+hairdresser.getId()+"/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        // 409 CONFLICT. Cita ya creada
        RequestAppointmentDTO request2 = new RequestAppointmentDTO();
        when(mockAppointmentService.createAppointment(hairdresser.getId(), request2)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/peluquerias/"+hairdresser.getId()+"/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void closeAppointmentTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/peluquerias/"+hairdresser.getId()
                                +"/citas/"+appointment.getId())
                        .param("hasReward", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserAppointmentsTest() throws Exception {
        // 404 NOT FOUND. Usuario no existe
        when(mockAppointmentService.findAllAppointmentsUser(client.getId())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/"+client.getId()+"/citas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // 204 NO CONTENT
        when(mockAppointmentService.findAllAppointmentsUser(client.getId())).thenReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/"+client.getId()+"/citas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 200 OK
        when(mockAppointmentService.findAllAppointmentsUser(client.getId())).thenReturn(List.of(appointment));
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/"+client.getId()+"/citas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    void getUserAppointmentTest() throws Exception {
        // 404 NOT FOUND
        when(mockAppointmentService.findById(client.getId(), 2)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/"+client.getId()+"/citas/"+2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // 200 OK
        when(mockAppointmentService.findById(client.getId(), appointment.getId())).thenReturn(appointment);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/"+client.getId()+"/citas/"+appointment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void getHistoryAppointmentsUserTest() throws Exception {
        // 404 NOT FOUND
        when(mockAppointmentService.getHistoryAppointmentsUser(client.getId())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/"+client.getId()+"/historico_citas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // 200 OK
        when(mockAppointmentService.getHistoryAppointmentsUser(client.getId())).thenReturn(List.of(appointment));
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/"+client.getId()+"/historico_citas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void updateAppointmentTest() throws Exception {
        Appointment updateAppointment = new Appointment();
        updateAppointment.setComment("Update Comment");

        // 400 BAD REQUEST
        when(mockAppointmentService.updateAppointment(client.getId(), appointment.getId(), updateAppointment))
                .thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/"+client.getId()+"/citas/"+appointment.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"comment\": \"Update Comment\" }"))
                .andExpect(status().isBadRequest());

        // 200 OK
        appointment.setComment(updateAppointment.getComment());
        when(mockAppointmentService.updateAppointment(client.getId(), appointment.getId(), updateAppointment))
                .thenReturn(appointment);
        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/"+client.getId()+"/citas/"+appointment.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"comment\": \"Update Comment\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Update Comment"));
    }

    @Test
    void deleteAppointmentTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/"+client.getId()
                        +"/citas/"+appointment.getId()))
                .andExpect(status().isNoContent());
    }
}