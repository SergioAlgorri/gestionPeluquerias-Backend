package gestionPeluqueria.entities;

import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.composite.CompositeService;
import gestionPeluqueria.entities.composite.SimpleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HairdresserCompanyTest {

    private HairdresserCompany emptyHairdresserCompany;
    private HairdresserCompany paramHairdresserCompany;

    private String name;

    @BeforeEach
    void setUp() {
        // Constructor vacío
        emptyHairdresserCompany = new HairdresserCompany();

        // Constructor parametrizado
        name = "Peluquería X";
        paramHairdresserCompany = new HairdresserCompany(name);
    }
    @Test
    void testDefaultConstructor() {
        assertNull(emptyHairdresserCompany.getName(), "The name should be null by default");
        assertEquals(0, emptyHairdresserCompany.getHairdressers().size(),
                "The hairdressers should be empty by default");
        assertEquals(0, emptyHairdresserCompany.getServices().size(),
                "The services should be empty by default");
        assertEquals(0, emptyHairdresserCompany.getRewards().size(),
                "The rewards should be empty by default");
        assertEquals(0, emptyHairdresserCompany.getClients().size(),
                "The clients should be empty by default");
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(name, paramHairdresserCompany.getName(), "The name should be equal to the expected value");
    }

    @Test
    void testSetAndGetName() {
        emptyHairdresserCompany.setName(name);
        assertEquals(name, emptyHairdresserCompany.getName(), "The name should be equal to the expected value");
    }

    @Test
    public void testAddAndGetHairdressers() {
        assertEquals(0, emptyHairdresserCompany.getHairdressers().size(),
                "The number of hairdressers should be equal to the expected value");

        Hairdresser hairdresser1 = new Hairdresser();
        Hairdresser hairdresser2 = new Hairdresser();
        emptyHairdresserCompany.addHairdresser(hairdresser1);
        emptyHairdresserCompany.addHairdresser(hairdresser2);

        assertEquals(2, emptyHairdresserCompany.getHairdressers().size(),
                "The number of hairdressers should be equal to the expected value");
        assertEquals(hairdresser1, emptyHairdresserCompany.getHairdressers().get(0),
                "The hairdresser should be equal to the expected value");
        assertEquals(hairdresser2, emptyHairdresserCompany.getHairdressers().get(1),
                "The hairdresser should be equal to the expected value");
    }

    @Test
    public void testAddAndGetClients() {
        assertEquals(0, emptyHairdresserCompany.getClients().size(),
                "The number of clients should be equal to the expected value");

        Client client = new Client();
        emptyHairdresserCompany.addClient(client);

        assertEquals(1, emptyHairdresserCompany.getClients().size(),
                "The number of clients should be equal to the expected value");
        assertEquals(client, emptyHairdresserCompany.getClients().get(0),
                "The client should be equal to the expected value");
    }

    @Test
    public void testAddAndGetServices() {
        assertEquals(0, emptyHairdresserCompany.getServices().size(),
                "The number of services should be equal to the expected value");

        SimpleService service1 = new SimpleService();
        SimpleService service2 = new SimpleService();
        SimpleService service3 = new SimpleService();
        CompositeService service4 = new CompositeService();
        service4.addService(service2);
        service4.addService(service3);

        emptyHairdresserCompany.addService(service1);
        emptyHairdresserCompany.addService(service2);
        emptyHairdresserCompany.addService(service3);
        emptyHairdresserCompany.addService(service4);

        assertEquals(4, emptyHairdresserCompany.getServices().size(),
                "The number of services should be equal to the expected value");
        assertEquals(service1, emptyHairdresserCompany.getServices().get(0),
                "The service should be equal to the expected value");
        assertEquals(service2, emptyHairdresserCompany.getServices().get(1),
                "The service should be equal to the expected value");
        assertEquals(service3, emptyHairdresserCompany.getServices().get(2),
                "The service should be equal to the expected value");
        assertEquals(service4, emptyHairdresserCompany.getServices().get(3),
                "The service should be equal to the expected value");
    }

    @Test
    public void testAddAndGetRewards() {
        assertEquals(0, emptyHairdresserCompany.getRewards().size(),
                "The number of rewards should be equal to the expected value");

        Reward reward = new Reward();
        emptyHairdresserCompany.addRewards(reward);

        assertEquals(1, emptyHairdresserCompany.getRewards().size(),
                "The number of rewards should be equal to the expected value");
        assertEquals(reward, emptyHairdresserCompany.getRewards().get(0),
                "The reward should be equal to the expected value");
    }

    @Test
    public void testEqualsAndHashCode() {
        HairdresserCompany hairdresserCompany2 = new HairdresserCompany("Peluquería X");
        HairdresserCompany hairdresserCompany3 = new HairdresserCompany("Peluquería Y");

        assertEquals(paramHairdresserCompany, hairdresserCompany2,
                "Hairdresser companies with the same values should be equal");
        assertNotEquals(hairdresserCompany2, hairdresserCompany3,
                "Hairdresser companies with different values should not be equal");
        assertEquals(paramHairdresserCompany.hashCode(), hairdresserCompany2.hashCode(),
                "Equal hairdresser companies should have the same hash code");
    }
}
