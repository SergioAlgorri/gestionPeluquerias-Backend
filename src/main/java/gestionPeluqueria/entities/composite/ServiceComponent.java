package gestionPeluqueria.entities.composite;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.math.BigDecimal;
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

    /**
     * Empty constructor.
     */
    public ServiceComponent() {

    }

    /**
     * Parameterised constructor.
     * @param name name of the service.
     * @param description description of the service.
     */
    public ServiceComponent(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters y Setter de los atributos de la clase

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

    /**
     * Method that returns the price of the service.
     * @return price of the service.
     */
    public abstract BigDecimal getPrice();

    /**
     * Method that returns an array with the durations of the service.
     * @return an array of durations of the service.
     */
    public abstract List<Integer> getDuration ();

    /**
     * Method that returns the total duration of the service.
     * @return total duration of the service.
     */
    public abstract int getTotalDuration();

    /**
     * Method initialising the price of the service.
     * @param price price of the service.
     */
    public abstract void setPrice(BigDecimal price);

    /**
     * method initialising the array of durations of the service
     * @param duration array with the durations of the service.
     */
    public abstract void setDuration(List<Integer> duration);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceComponent that = (ServiceComponent) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "ServiceComponent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}