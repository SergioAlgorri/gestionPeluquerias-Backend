package gestionPeluqueria.entities;

import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.composite.ServiceComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AppointmentTest {

    private Appointment emptyAppointment;
    private Appointment paramAppointment;
    private LocalDateTime startTime;
    private String comment;

    private ServiceComponent mockService;
    private Employee mockEmployee;
    private Reward mockReward;
    private Client mockClient;
    private Hairdresser mockHairdresser;

    @BeforeEach
    void setUp() {
        // Creación de los objetos Mock
        mockService = Mockito.mock(ServiceComponent.class);
        mockEmployee = Mockito.mock(Employee.class);
        mockReward = Mockito.mock(Reward.class);
        mockClient = Mockito.mock(Client.class);
        mockEmployee = Mockito.mock(Employee.class);
        mockHairdresser = Mockito.mock(Hairdresser.class);

        // Programación del comportamiento de los objetos Mock
        when(mockService.getTotalDuration()).thenReturn(60);
        when(mockService.getPrice()).thenReturn(new BigDecimal("100.00"));
        when(mockReward.getDiscountAmount()).thenReturn(new BigDecimal("0.80")); // 20% de descuento

        // Constructor vacío
        emptyAppointment = new Appointment();

        // Constructor parametrizado
        startTime = LocalDateTime.of(2024, 12, 1, 10, 0);
        comment = "Primera cita";
        paramAppointment = new Appointment(startTime, comment, mockClient, mockEmployee, mockService,
                mockReward, mockHairdresser);
    }

    @Test
    void testDefaultConstructor() {
        assertNull(emptyAppointment.getStartTime(), "The start time should be null by default");
        assertNull(emptyAppointment.getComment(), "The comment should be null by default");
        assertNull(emptyAppointment.getUser(), "The client should be null by default");
        assertNull(emptyAppointment.getEmployee(), "The employee should be null by default");
        assertNull(emptyAppointment.getService(), "The service should be null by default");
        assertNull(emptyAppointment.getReward(), "The reward should be null by default");
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(startTime, paramAppointment.getStartTime(),
                "The start time should be equal to expected value");
        assertEquals(comment, paramAppointment.getComment(),
                "The comment should be equal to expected value");
        assertEquals(mockClient, paramAppointment.getUser(), "The client should be equal to expected value");
        assertEquals(mockEmployee, paramAppointment.getEmployee(),
                "The employee should be equal to expected value");
        assertEquals(mockService, paramAppointment.getService(),
                "The service should be equal to expected value");
        assertEquals(mockReward, paramAppointment.getReward(), "The reward should be equal to expected value");
        assertEquals(mockHairdresser, paramAppointment.getHairdresser(),
                "The hairdresser should be equal to expected value");
    }

    @Test
    void testGetAndSetStartTime() {
        emptyAppointment.setStartTime(startTime);
        assertEquals(startTime, emptyAppointment.getStartTime(),
                "The start time should be equal to the expected value");
    }

    @Test
    void testGetAndSetComment() {
        emptyAppointment.setComment(comment);
        assertEquals(comment, emptyAppointment.getComment(),
                "The comment should be equal to the expected value");
    }

    @Test
    void testGetAndSetClient() {
        emptyAppointment.setUser(mockClient);
        assertEquals(mockClient, emptyAppointment.getUser(),
                "The client should be equal to the expected value");
    }

    @Test
    void testGetAndSetEmployee() {
        emptyAppointment.setService(mockService);
        assertEquals(mockService, emptyAppointment.getService(),
                "The service should be equal to the expected value");
    }

    @Test
    void testGetAndSetReward() {
        emptyAppointment.setReward(mockReward);
        assertEquals(mockReward, emptyAppointment.getReward(),
                "The reward should be equal to the expected value");
    }

    @Test
    void testGetAndSetHairdresser() {
        emptyAppointment.setHairdresser(mockHairdresser);
        assertEquals(mockHairdresser, emptyAppointment.getHairdresser(),
                "The hairdresser should be equal to the expected value");
    }

    @Test
    void testCalculateEndingTime() {
        LocalDateTime expectedEndTime = startTime.plusMinutes(60);
        assertEquals(expectedEndTime, paramAppointment.getEndTime(),
                "The end time should be equal to the expected value");
    }

    @Test
    void testCalculatePriceWithReward() {
        BigDecimal expectedPrice = new BigDecimal("80.00"); // 100 * 0.80
        assertEquals(expectedPrice, paramAppointment.calculatePrice(),
                "The price should be equal to the expected value");
    }

    @Test
    void testCalculatePriceWithoutReward() {
        paramAppointment.setReward(null);
        BigDecimal expectedPrice = new BigDecimal("100.00");
        assertEquals(expectedPrice, paramAppointment.calculatePrice(),
                "The price should be equal to the expected value");
    }

    @Test
    void testCalculatePointsWithReward() {
        int expectedPoints = 0;
        assertEquals(expectedPoints, paramAppointment.calculatePoints(),
                "The points should be equal to the expected value");
    }

    @Test
    void testCalculatePointsWithoutReward() {
        paramAppointment.setReward(null);
        int expectedPoints = 10; // 10% de 100 = 10 puntos
        assertEquals(expectedPoints, paramAppointment.calculatePoints(),
                "The points should be equal to the expected value");
    }

    @Test
    void testEqualsAndHashCode() {
        Appointment appointment2 = new Appointment(startTime, "Primera cita", mockClient, mockEmployee,
                mockService, mockReward, mockHairdresser);
        Appointment appointment3 = new Appointment(startTime.plusDays(1), "Segunda cita", mockClient,
                mockEmployee, mockService, mockReward, mockHairdresser);

        assertEquals(paramAppointment, appointment2, "Appointments with the same values should be equal");
        assertNotEquals(appointment2, appointment3, "Appointments with different values should not be equal");
        assertEquals(paramAppointment.hashCode(), appointment2.hashCode(),
                "Equal appointments should have the same hash code");
    }
}
