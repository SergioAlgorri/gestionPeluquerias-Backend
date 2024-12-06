package gestionPeluqueria.entities.composite;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "composite_service")
public class CompositeService extends ServiceComponent {

    @OneToMany
    @JoinTable(name = "composite_service_services",
            joinColumns = @JoinColumn(name = "composite_service_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ServiceComponent> services = new ArrayList<>();

    public CompositeService(String name, String description) {
        super(name, description);
    }

    public CompositeService() {

    }

    public void addService(ServiceComponent service) {
        services.add(service);
    }

    public void removeService(ServiceComponent service) {
        services.remove(service);
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal total = new BigDecimal("0.00");
        for (ServiceComponent sc: services) {
            if (sc instanceof SimpleService) {
                total = total.add(((SimpleService) sc).getPrice());
            }
        }

        return total;
    }

    @Override
    public List<Integer> getDuration() {
        List<Integer> duration = new ArrayList<>();
        for (ServiceComponent sc: services) {
            if (sc instanceof SimpleService) {
                duration.addAll(((SimpleService) sc).getDuration());
                duration.add(0);    // Duración 0 añadida para que las posiciones del array 0,2,4,... sean trabajo
            }                       // y las otras posiciones (1,3,5,...) sean descanso.
        }

        duration.remove(duration.size() - 1);   // Elimina el último 0 añadido
        return duration;
    }

    @Override
    public int getTotalDuration() {
        int total = 0;
        for (ServiceComponent s: services) {
            total += s.getTotalDuration();
        }

        return total;
    }
}
