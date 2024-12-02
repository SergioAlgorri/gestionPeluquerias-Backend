package gestionPeluqueria.entities.composite;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "simple_service")
public class SimpleService extends ServiceComponent {

    public SimpleService(String name, String description, BigDecimal price, List<Integer> duration) {
        super(name, description, price, duration);
    }

    public SimpleService() {

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
