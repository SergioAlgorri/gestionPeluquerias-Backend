package gestionPeluqueria.entities.composite;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity(name = "service_component")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract  class ServiceComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    protected String name;
    protected String description;

    public ServiceComponent(String name, String description) {
        this.name = name;
        this.description = description;
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

    public abstract BigDecimal getPrice();
    public abstract List<Integer> getDuration ();
    public abstract int getTotalDuration();

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
