package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.RequestAppointmentDTO;
import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Reward;
import gestionPeluqueria.entities.composite.ServiceComponent;
import gestionPeluqueria.entities.composite.SimpleService;
import gestionPeluqueria.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository mockAppointmentRepository;
    @Mock
    private HairdresserRepository mockHairdresserRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ServiceRepository mockServiceRepository;
    @Mock
    private RewardRepository mockRewardRepository;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Hairdresser hairdresser;
    private Employee employee;
    private User client;
    private Appointment appointment;
    private ServiceComponent service;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentService = new AppointmentServiceImpl(mockAppointmentRepository, mockHairdresserRepository,
                mockUserRepository, mockServiceRepository, mockRewardRepository);

        // Crear peluquería
        hairdresser = new Hairdresser(LocalTime.of(9, 0), LocalTime.of(18, 0),
                "Calle Ejemplo, 123", "123456789");
        hairdresser.setId(1);

        // Crear empleado
        employee = new Employee("John", "Doe", "Smith", "john.doe@example.com",
                "password123", LocalDate.of(1990, 5, 15), "987654321");
        employee.setId(2L);
        hairdresser.getEmployees().add(employee);

        // Crear cliente
        client = new Client("Jane", "Doe", "Smith", "jane.doe@example.com",
                "securepass", LocalDate.of(1985, 3, 10), "1122334455");
        client.setId(3);

        // Crear cita
        service = new SimpleService("Corte", "Corte básico",
                new BigDecimal("15.00"), List.of(30));
        service.setId(5);
        appointment = new Appointment(LocalDateTime.of(2024, 12, 20, 10, 0),
                "Corte de pelo", client, employee, service,null, hairdresser);
        appointment.setId(4);

        testDate = LocalDate.of(2024, 12, 20);
    }

    @Test
    void findAllTest() {
        // Peluqueria no existe
        when(mockHairdresserRepository.findById(1)).thenReturn(null);
        List<Appointment> resultHairdresserNull =
                appointmentService.findAll(1, null, null);
        assertNull(resultHairdresserNull,
                "The list of appointments should be null (Hairdresser does not exist)");

        // Búsqueda por peluquería (idEmployee null y LocalDate null (por lo que DATE es el día de hoy))
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockAppointmentRepository.findByHairdresser(1, LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(23, 59, 59))).thenReturn(List.of());

        List<Appointment> resultEmployeeAndDateNull =
                appointmentService.findAll(1, null, null);
        assertNotNull(resultEmployeeAndDateNull, "The list of appointments should not be null");
        assertEquals(0, resultEmployeeAndDateNull.size(),
                "There are no appointments for the selected day");

        verify(mockHairdresserRepository, times(2)).findById(1);
        verify(mockAppointmentRepository).findByHairdresser(1, LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(23, 59, 59));

        // Búsqueda por peluquería (idEmployee no null y LocalDate null (por lo que DATE es el día de hoy))
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockUserRepository.findById(2)).thenReturn(employee);
        when(mockAppointmentRepository.findByHairdresserAndEmployeeId(1, 2L, testDate.atStartOfDay(),
                testDate.atTime(23, 59, 59))).thenReturn(List.of(appointment));

        List<Appointment> resultDateNull =
                appointmentService.findAll(1, 2L, null);

        assertNotNull(resultDateNull, "The list of appointments should not be null");
        assertEquals(0, resultDateNull.size(),
                "There are no appointments for the selected day");

        verify(mockHairdresserRepository, times(3)).findById(1);
        verify(mockAppointmentRepository).findByHairdresserAndEmployeeId(1, 2L,
                LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59));

        // Búsqueda por peluquería (idEmployee null pero LocalDate no null)
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockAppointmentRepository.findByHairdresser(1, testDate.atStartOfDay(),
                testDate.atTime(23, 59, 59))).thenReturn(List.of(appointment));

        List<Appointment> resultEmployeeNull = appointmentService.findAll(1, null, testDate);

        assertEquals(1, resultEmployeeNull.size(), "There is one appointment for the selected day");
        assertEquals(appointment, resultEmployeeNull.get(0), "The appointments should be equals");

        verify(mockHairdresserRepository, times(4)).findById(1);
        verify(mockAppointmentRepository).findByHairdresser(1, testDate.atStartOfDay(),
                testDate.atTime(23, 59, 59));

        // Búsqueda por peluquería (idEmployee y LocalDate no null)
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockUserRepository.findById(2)).thenReturn(employee);
        when(mockAppointmentRepository.findByHairdresserAndEmployeeId(1, 2L, testDate.atStartOfDay(),
                testDate.atTime(23, 59, 59))).thenReturn(List.of(appointment));

        List<Appointment> result = appointmentService.findAll(1, 2L, testDate);

        assertEquals(1, result.size(), "There is one appointment for the selected day");
        assertEquals(appointment, result.get(0));

        verify(mockHairdresserRepository, times(5)).findById(1);
        verify(mockAppointmentRepository).findByHairdresserAndEmployeeId(1, 2L,
                testDate.atStartOfDay(), testDate.atTime(23, 59, 59));
    }

    @Test
    void findAllAppointmentsUserTest() {
        // Cliente no valido
        when(mockUserRepository.findById(client.getId())).thenReturn(null);

        List<Appointment> resultNull = appointmentService.findAllAppointmentsUser(client.getId());

        verify(mockUserRepository).findById(client.getId());
        assertNull(resultNull, "The result should be null (User does not exist)");

        // Cliente Valido
        List<Appointment> appointments = List.of(
                new Appointment(),
                new Appointment()
        );

        when(mockUserRepository.findById(client.getId())).thenReturn(client);
        when(mockAppointmentRepository.findByUser(client.getId())).thenReturn(appointments);

        List<Appointment> result = appointmentService.findAllAppointmentsUser(client.getId());

        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "There are two appointments");
        verify(mockUserRepository, times(2)).findById(client.getId());
        verify(mockAppointmentRepository).findByUser(client.getId());
    }

    @Test
    void findByIdTest() {
        // Usuario no existe
        when(mockUserRepository.findById(3)).thenReturn(null);
        Appointment appointment1 = appointmentService.findById(3, 4);
        assertNull(appointment1, "The appointment should be null (User does not exist)");

        // Cita no existe
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(4)).thenReturn(null);
        Appointment appointment2 = appointmentService.findById(3, 4);
        assertNull(appointment2, "The appointment should be null (Appointment does not exist)");

        // Cliente no tiene citas
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        Appointment appointment3 = appointmentService.findById(3, 4);
        assertNull(appointment3, "The appointment should be null (User does not have appointments)");

        // Caso Válido
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        ((Client) client).addAppointment(appointment);
        Appointment appointment4 = appointmentService.findById(3, 4);
        assertNotNull(appointment4, "The appointment should not be null");
        assertEquals(appointment, appointment4, "The appointments should be equals");
    }

    @Test
    void createAppointmentTest() {
        RequestAppointmentDTO request = new RequestAppointmentDTO();
        request.setIdService(service.getId());
        request.setIdUser(client.getId());
        request.setIdEmployee(employee.getId());
        request.setStartTime(appointment.getStartTime());

        // Peluqueria no existe
        when(mockHairdresserRepository.findById(1)).thenReturn(null);
        Appointment createdAppointment = appointmentService.createAppointment(1, request);
        assertNull(createdAppointment, "Appointment should be null (Hairdresser does not exist)");

        // Servicio no existe
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockServiceRepository.findById(5)).thenReturn(null);
        Appointment createdAppointment2 = appointmentService.createAppointment(1, request);
        assertNull(createdAppointment2, "Appointment should be null (Service does not exist)");

        // Usuario no existe
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockServiceRepository.findById(5)).thenReturn(service);
        when(mockUserRepository.findById(3)).thenReturn(null);
        Appointment createdAppointment3 = appointmentService.createAppointment(1, request);
        assertNull(createdAppointment3, "Appointment should be null (User does not exist)");

        // No hay disponibilidad
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockServiceRepository.findById(5)).thenReturn(service);
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findByHairdresser(anyLong(), any(), any())).thenReturn(List.of(appointment));
        Appointment createdAppointment4 = appointmentService.createAppointment(1, request);
        assertNull(createdAppointment4, "Appointment should be null (Hairdresser not available)");

        // Empleado no disponible
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockServiceRepository.findById(5)).thenReturn(service);
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockUserRepository.findById(2L)).thenReturn(employee);
        when(mockAppointmentRepository.findByHairdresserAndEmployeeId(anyLong(), anyLong(), any(), any()))
                .thenReturn(List.of(appointment));
        Appointment createdAppointment5 = appointmentService.createAppointment(1, request);
        assertNull(createdAppointment5, "Appointment should be null (Employee not available)");

        // Caso Válido
        assertEquals(0, ((Client) client).getAppointments().size(),
                "The number of appointments should be equal to the expected value");
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockServiceRepository.findById(5)).thenReturn(service);
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockUserRepository.findById(2)).thenReturn(employee);
        when(mockAppointmentRepository.findByHairdresser(anyLong(), any(), any())).thenReturn(List.of());
        when(mockAppointmentRepository.findByHairdresserAndEmployeeId(anyLong(), anyLong(), any(), any()))
                .thenReturn(List.of());
        Appointment createdAppointment6 = appointmentService.createAppointment(1, request);
        assertNotNull(createdAppointment6, "Appointment should not be null (Appointment Created)");
    }

    @Test
    void updateAppointmentTest() {
        Appointment update = new Appointment();
        update.setComment("Comentario Actualizado");

        // Cliente no existe
        when(mockUserRepository.findById(3)).thenReturn(null);
        Appointment updateAppointment = appointmentService.updateAppointment(3, 4, update);
        assertNull(updateAppointment, "The appointment should be null (User does not exist)");

        // Cita no existe
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(4)).thenReturn(null);
        Appointment updateAppointment2 = appointmentService.updateAppointment(3, 4, update);
        assertNull(updateAppointment2, "The appointment should be null (Appointment does not exist)");

        // Usuario no tiene la cita que se desea actualizar
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        Appointment updateAppointment3 = appointmentService.updateAppointment(3, 4, update);
        assertNull(updateAppointment3, "The appointment should be null (User does not have appointments)");

        // Caso Válido
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        ((Client) client).addAppointment(appointment);
        when(mockAppointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        Appointment updateAppointment4 = appointmentService.updateAppointment(3, 4, update);
        assertNotNull(updateAppointment4, "The appointment should not be null");
        assertEquals(update.getComment(), updateAppointment4.getComment(),
                "The comment should be equal to the expected value");
    }

    @Test
    void deleteAppointmentTest() {
        // Usuario no existe
        when(mockUserRepository.findById(3)).thenReturn(null);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        assertThrows(IllegalArgumentException.class, () ->
            {appointmentService.deleteAppointment(3, 4);}, "User does not exist");
        verify(mockAppointmentRepository, never()).delete(appointment);

        // Cita no existe
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(1)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () ->
            {appointmentService.deleteAppointment(3, 1);}, "Appointment does not exist");
        verify(mockAppointmentRepository, never()).delete(appointment);

        // Usuario no le corresponde la cita
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        assertThrows(IllegalArgumentException.class, () ->
            {appointmentService.deleteAppointment(3, 4);}, "User without appointments");
        verify(mockAppointmentRepository, never()).delete(appointment);

        // Caso Válido
        when(mockUserRepository.findById(3)).thenReturn(client);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        ((Client) client).addAppointment(appointment);
        appointmentService.deleteAppointment(3, 4);
        verify(mockAppointmentRepository).delete(appointment);
    }

    @Test
    void getHistoryAppointmentsUserTest() {
        // Usuario no existe
        when(mockUserRepository.findById(3)).thenReturn(null);
        List<Appointment> history = appointmentService.getHistoryAppointmentsUser(3);
        assertNull(history, "The history of appointments should be null (User does not exist)");

        // Usuario no es un cliente
        when(mockUserRepository.findById(3)).thenReturn(new Employee());
        List<Appointment> history2 = appointmentService.getHistoryAppointmentsUser(3);
        assertNull(history2, "The history of appointments should be null (User is not a Client)");

        // Caso Váludo
        when(mockUserRepository.findById(3)).thenReturn(client);
        ((Client) client).addHistory(appointment);
        List<Appointment> history3 = appointmentService.getHistoryAppointmentsUser(3);
        assertNotNull(history3, "The history of appointments should not be null");
        assertEquals(1, history3.size(),
                "The number of appointments should be equal to the expected value");
    }

    @Test
    void closeAppointmentTest() {
        // Peluqueria no existe
        when(mockHairdresserRepository.findById(1)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () ->
            {appointmentService.closeAppointment(1, 4, false);},
                "Hairdresser does not exist");

        // Cita no existe
        when(mockAppointmentRepository.findById(4)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () ->
                {appointmentService.closeAppointment(1, 4, false);},
                "Appointment does not exist");

        // Cita no pertenece a la peluquería
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        assertThrows(IllegalArgumentException.class, () ->
                {appointmentService.closeAppointment(1, 4, false);},
                "Appointment not belonging to hairdresser");

        // Cita ya cerrada
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        appointment.setHairdresser(hairdresser);
        appointment.setUser(client);
        ((Client) client).addHistory(appointment);
        assertThrows(IllegalArgumentException.class, () ->
                {appointmentService.closeAppointment(1, 4, false);},
                "Appointment already close");
        ((Client) client).getHistory().remove(appointment);

        // Caso Válido Sin Recompensa
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        hairdresser.addAppointments(appointment);
        appointment.setUser(client);
        ((Client) client).setPoints(0);
        appointmentService.closeAppointment(1,4, false);
        assertEquals(1, ((Client) client).getPoints(),
                "The points should be equal to the expected value");
        assertEquals(service.getPrice(), appointment.getPrice(), "The price should be equal");
        assertEquals(0, ((Client) client).getAppointments().size(), "There are no appointments");
        assertEquals(appointment, ((Client) client).getHistory().get(0), "Appointment in the client's history");
        ((Client) client).getHistory().remove(appointment);

        // Caso Válido con Recompensa
        when(mockHairdresserRepository.findById(1)).thenReturn(hairdresser);
        when(mockAppointmentRepository.findById(4)).thenReturn(appointment);
        hairdresser.addAppointments(appointment);
        appointment.setUser(client);
        ((Client) client).setPoints(100);
        Reward reward = new Reward();
        reward.setPoints(20);
        reward.setDiscountAmount(new BigDecimal("0.80"));
        appointment.setReward(reward);
        appointmentService.closeAppointment(1,4, true);
        assertEquals(service.getPrice().multiply(reward.getDiscountAmount()).setScale(2, RoundingMode.DOWN),
                appointment.getPrice(),"The price should be equal");
        assertEquals(80, ((Client) client).getPoints(),
                "The points should be equal to the expected value");
        assertEquals(0, ((Client) client).getAppointments().size(), "There are no appointments");
        assertEquals(appointment, ((Client) client).getHistory().get(0), "Appointment in the client's history");
    }
}
