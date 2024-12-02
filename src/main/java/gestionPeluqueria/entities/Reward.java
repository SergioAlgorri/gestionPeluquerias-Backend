package gestionPeluqueria.entities;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private BigDecimal discount;
    private Integer points;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    public Reward() {

    }

    public Reward(String name, BigDecimal discount, Integer points, LocalDate expirationDate) {
        this.name = name;
        this.discount = discount;
        this.points = points;
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDiscountAmount() {
        return discount;
    }

    public void setDiscountAmount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reward reward = (Reward) o;
        return points == reward.points && Objects.equals(name, reward.name)
                && Objects.equals(discount, reward.discount)
                && Objects.equals(expirationDate, reward.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, discount, points, expirationDate);
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", discountAmount=" + discount +
                ", points=" + points +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
