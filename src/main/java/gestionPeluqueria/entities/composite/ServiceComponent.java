package gestionPeluqueria.entities.composite;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "service_component")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleService.class, name = "simple"),
        @JsonSubTypes.Type(value = CompositeService.class, name = "composite")
})
public abstract  class ServiceComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    protected String name;
    protected String description;
    protected BigDecimal price;

    @ElementCollection
    @CollectionTable(name = "service_duration", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "duration_minutes")
    protected List<Integer> duration = new ArrayList<>();

    public ServiceComponent(String name, String description, BigDecimal price, List<Integer> duration) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    public ServiceComponent() {

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

    public abstract int getTotalDuration();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceComponent that = (ServiceComponent) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration);
    }

    @Override
    public String toString() {
        return "ServiceComponent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                '}';
    }
}
