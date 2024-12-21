package gestionPeluqueria.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class RequestServiceDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private List<Integer> duration;
    private List<Long> services;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<Integer> getDuration() {
        return duration;
    }

    public void setDuration(List<Integer> duration) {
        this.duration = duration;
    }

    public List<Long> getServices() {
        return services;
    }

    public void setServices(List<Long> services) {
        this.services = services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestServiceDTO that = (RequestServiceDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description)
                && Objects.equals(price, that.price) && Objects.equals(duration, that.duration)
                && Objects.equals(services, that.services);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration, services);
    }
}