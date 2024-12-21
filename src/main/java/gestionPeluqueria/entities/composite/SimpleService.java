package gestionPeluqueria.entities.composite;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "simple_service")
public class SimpleService extends ServiceComponent {

    private BigDecimal price;

    @ElementCollection
    @CollectionTable(name = "service_duration", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "duration_minutes")
    private List<Integer> duration = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public SimpleService() {

    }

    /**
     * Parameterised constructor.
     * Same constructor of the parent class by adding the price and the duration to it.
     */
    public SimpleService(String name, String description, BigDecimal price, List<Integer> duration) {
        super(name, description);
        this.price = price;
        this.duration = duration;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public List<Integer> getDuration() {
        return duration;
    }

    @Override
    public void setDuration(List<Integer> duration) {
        this.duration = duration;
    }

    @Override
    public int getTotalDuration() {
        int total = 0;
        for (int d: duration) {
            total += d;
        }

        return total;
    }
}