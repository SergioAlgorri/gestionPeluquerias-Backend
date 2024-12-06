package gestionPeluqueria.entities;

import gestionPeluqueria.entities.Inheritance.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class HairdresserTest {
    private Hairdresser emptyHairdresser;
    private Hairdresser paramHairdresser;

    private LocalTime openingTime;
    private LocalTime closingTime;
    private String address;
    private String telephone;
    private HairdresserCompany mockHairdresserCompany;

    @BeforeEach
    void setUp() {
        // Creación de los objetos Mock
        mockHairdresserCompany = Mockito.mock(HairdresserCompany.class);

        // Constructor vacío
        emptyHairdresser = new Hairdresser();

        // Constructor parametrizado
        openingTime = LocalTime.of(9,0,0);
        closingTime = LocalTime.of(20,0,0);
        address = "C/Gutierrez Solana";
        telephone = "671134561";

        paramHairdresser = new Hairdresser(openingTime, closingTime, address, telephone);
    }

    @Test
    void testDefaultConstructor() {
        assertNull(emptyHairdresser.getOpeningTime(), "The opening time should be null by default");
        assertNull(emptyHairdresser.getClosingTime(), "The closing time should be null by default");
        assertNull(emptyHairdresser.getAddress(), "The address should be null by default");
        assertNull(emptyHairdresser.getTelephone(), "The telephone should be null by default");
        assertNull(emptyHairdresser.getCompany(), "The company should be null by default");
        assertEquals(0, emptyHairdresser.getEmployees().size(),
                "The employees should be empty by default");
        assertEquals(0, emptyHairdresser.getAppointments().size(),
                "The appointments should be empty by default");

    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(openingTime, paramHairdresser.getOpeningTime(),
                "The opening time should be equal to the expected value");
        assertEquals(closingTime, paramHairdresser.getClosingTime(),
                "The closing time should be equal to the expected value");
        assertEquals(address, paramHairdresser.getAddress(),
                "The address should be equal to the expected value");
        assertEquals(telephone, paramHairdresser.getTelephone(),
                "The telephone should be equal to the expected value");
        assertEquals(0, emptyHairdresser.getEmployees().size(),
                "The employees should be empty by default");
        assertEquals(0, emptyHairdresser.getAppointments().size(),
                "The appointments should be empty by default");
    }

    @Test
    void testSetAndGetOpeningTime() {
        emptyHairdresser.setOpeningTime(openingTime);
        assertEquals(openingTime, emptyHairdresser.getOpeningTime(),
                "The opening time should be equal to the expected value");
    }

    @Test
    void testSetAndGetClosingTime() {
        emptyHairdresser.setClosingTime(closingTime);
        assertEquals(closingTime, emptyHairdresser.getClosingTime(),
                "The closing time should be equal to the expected value");
    }

    @Test
    void testSetAndGetAddress() {
        emptyHairdresser.setAddress(address);
        assertEquals(address, emptyHairdresser.getAddress(),
                "The address should be equal to the expected value");
    }

    @Test
    void testSetAndGetTelephone() {
        emptyHairdresser.setTelephone(telephone);
        assertEquals(telephone, emptyHairdresser.getTelephone(),
                "The telephone should be equal to the expected value");
    }

    @Test
    void testSetAndGetCompany() {
        emptyHairdresser.setCompany(mockHairdresserCompany);
        assertEquals(mockHairdresserCompany, emptyHairdresser.getCompany(),
                "The company should be equal to the expected value");
    }

    @Test
    void testAddAndGetEmployees() {
        assertEquals(0, emptyHairdresser.getEmployees().size(),
                "The number of employees should be equal to the expected value");

        Employee employee = new Employee();
        emptyHairdresser.addEmployee(employee);

        assertEquals(1, emptyHairdresser.getEmployees().size(),
                "The number of employees should be equal to the expected value");
        assertEquals(employee, emptyHairdresser.getEmployees().get(0),
                "The employee should be equal to the expected value");
    }

    @Test
    void testAddAndGetAppointments() {
        assertEquals(0, emptyHairdresser.getEmployees().size(),
                "The number of employees should be equal to the expected value");

        Employee employee = new Employee();
        emptyHairdresser.addEmployee(employee);

        assertEquals(1, emptyHairdresser.getEmployees().size(),
                "The number of employees should be equal to the expected value");
        assertEquals(employee, emptyHairdresser.getEmployees().get(0),
                "The employee should be equal to the expected value");
    }

    @Test
    public void testEqualsAndHashCode() {
        Hairdresser hairdresser2 = new Hairdresser(openingTime, closingTime, address, telephone);
        Hairdresser hairdresser3 = new Hairdresser(openingTime.plusHours(1),
                closingTime, address, telephone);

        assertEquals(paramHairdresser, hairdresser2, "Hairdressers with the same values should be equal");
        assertNotEquals(paramHairdresser, hairdresser3,
                "Hairdressers with different values should not be equal");
        assertEquals(paramHairdresser.hashCode(), hairdresser2.hashCode(),
                "Equal hairdressers should have the same hash code");
    }
}
