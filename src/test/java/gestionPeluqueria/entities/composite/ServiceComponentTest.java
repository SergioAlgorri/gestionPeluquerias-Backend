package gestionPeluqueria.entities.composite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceComponentTest {

    SimpleService service1;
    String name;
    String description;
    List<Integer> duration;

    SimpleService service2;
    String name2;
    String description2;
    List<Integer> duration2;

    CompositeService compositeService;
    String name3;
    String description3;

    @BeforeEach
    void setUp() {
        name = "Tinte Adulto";
        description = "Tinte de Pelo Adulto";
        duration = new ArrayList<>() {{
            add(15);
            add(30);
            add(15);
        }};
        service1 =  new SimpleService(name, description, ServicePrices.HAIR_COLOR_PRICE, duration);

        name2 = "Corte Adulto";
        description2 = "Corte de Pelo Adulto";
        duration2 = new ArrayList<>() {{
            add(30);
        }};
        service2 =  new SimpleService(name2, description2, ServicePrices.HAIRCUT_PRICE, duration2);

        name3 = "Corte y Tinte Adulto";
        description3 = "Corte y Tinte de Pelo Adulto";
        compositeService = new CompositeService(name3, description3);
        compositeService.addService(service1);
        compositeService.addService(service2);
    }

    @Test
    void testSimpleService() {
        assertEquals(name, service1.getName(), "The name should be equal to the expected value");
        assertEquals(description, service1.getDescription(),
                "The description should be equal to the expected value");
        assertEquals(ServicePrices.HAIR_COLOR_PRICE, service1.getPrice(),
                "The price should be equal to the expected value");
        assertEquals(duration, service1.getDuration(), "The duration should be equal to the expected value");

        assertEquals(60, service1.getTotalDuration(), "The total duration should be 60");
    }

    @Test
    void testCompositeService() {
        assertEquals(name3, compositeService.getName(), "The name should be equal to the expected value");
        assertEquals(description3, compositeService.getDescription(),
                "The description should be equal to the expected value");
        assertEquals(service1.getPrice().add(service2.getPrice()), compositeService.getPrice(),
                "The price should be equal to the expected value");
        List<Integer> expectedDuration = new ArrayList<>(List.of(15, 30, 15, 0, 30));
        assertEquals(expectedDuration, compositeService.getDuration(),
                "The duration should be equal to the expected value");
        assertEquals(service1.getTotalDuration()+service2.getTotalDuration(),
                compositeService.getTotalDuration(), "The total duration should be equal to the expected value");
    }

    @Test
    void testEqualsAndHashCode() {
        SimpleService service3 = new SimpleService("Tinte Adulto", "Tinte de Pelo Adulto",
                ServicePrices.HAIR_COLOR_PRICE, new ArrayList<>(List.of(15,30,15)));

        assertEquals(service1, service3, "Services with the same values should be equal");
        assertNotEquals(service1, service2, "Services with different values should not be equal");
        assertEquals(service1.hashCode(), service3.hashCode(), "Equal services should have the same hash code");
    }
}
