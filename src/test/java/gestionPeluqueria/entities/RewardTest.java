package gestionPeluqueria.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewardTest {

    private Reward emptyReward;
    private Reward paramReward;

    private String name;
    private BigDecimal discountAmount;
    private Integer points;
    private LocalDate expirationDate;

    @BeforeEach
    void setUp() {
        // Constructor vacío
        emptyReward = new Reward();

        // Constructor parametrizado
        name = "5% de descuento";
        discountAmount = new BigDecimal("0.95");
        points = 100;
        expirationDate = LocalDate.of(2025, 6, 4);
        paramReward = new Reward(name, discountAmount, points, expirationDate);
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(emptyReward.getName(), "The name should be null by default");
        assertNull(emptyReward.getDiscountAmount(), "The discount amount should be null by default");
        assertNull(emptyReward.getPoints(), "The points should be null by default");
        assertNull(emptyReward.getExpirationDate(), "The expiration date should be null by default");
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals(name, paramReward.getName(), "The name should be equal to the expected value");
        assertEquals(discountAmount, paramReward.getDiscountAmount(),
                "The discount amount should be equal to the expected value");
        assertEquals(points, paramReward.getPoints(), "The points should be equal to the expected value");
        assertEquals(expirationDate, paramReward.getExpirationDate(),
                "The expiration date should be equal to the expected value");
    }

    @Test
    public void testSetAndGetName() {
        emptyReward.setName(name);
        assertEquals(name, emptyReward.getName(), "The name should be equal to the expected value");
    }

    @Test
    public void testSetAndGetDiscountAmount() {
        emptyReward.setDiscountAmount(discountAmount);
        assertEquals(discountAmount, emptyReward.getDiscountAmount(), "The discount amount should be equal to the expected value");
    }

    @Test
    public void testSetAndGetPoints() {
        emptyReward.setPoints(points);
        assertEquals(points, emptyReward.getPoints(), "The ponints should be equal to the expected value");
    }

    @Test
    public void testSetAndGetExpirationDate() {
        emptyReward.setExpirationDate(expirationDate);
        assertEquals(expirationDate, emptyReward.getExpirationDate(),
                "The expiration date should be equal to the expected value");
    }

    @Test
    public void testEqualsAndHashCode() {
        Reward reward2 = new Reward("5% de descuento", new BigDecimal("0.95"), 100,
                LocalDate.of(2025, 6,4));
        Reward reward3 = new Reward("20% de descuento", new BigDecimal("80"), 1250,
                LocalDate.of(2026, 10,28));

        assertEquals(paramReward, reward2, "Rewards with the same values should be equal");
        assertNotEquals(paramReward, reward3, "Rewards with different values should not be equal");
        assertEquals(paramReward.hashCode(), reward2.hashCode(), "Equal services should have the same hash code");
    }
}
