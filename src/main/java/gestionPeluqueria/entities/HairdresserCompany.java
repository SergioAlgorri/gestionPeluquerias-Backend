package gestionPeluqueria.entities;

import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.composite.ServiceComponent;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "hairdresser_company")
public class HairdresserCompany implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(mappedBy = "company")
    private List<Hairdresser> hairdressers = new ArrayList<>();

    @Transient
    private List<ServiceComponent> services = new ArrayList<>();

    @Transient
    private List<Reward> rewards = new ArrayList<>();

    @Transient
    private List<Client> clients = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public HairdresserCompany() {

    }

    /**
     * Parameterised constructor.
     * @param name name of the hairdresser company
     */
    public HairdresserCompany(String name) {
        this.name = name;
    }

    // Getters y Setter de los atributos de la clase

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void addRewards(Reward reward) {
        this.rewards.add(reward);
    }

    public List<ServiceComponent> getServices() {
        return services;
    }

    public void addService(ServiceComponent service) {
        this.services.add(service);
    }

    public List<Hairdresser> getHairdressers() {
        return hairdressers;
    }

    public void addHairdresser(Hairdresser hairdresser) {
        this.hairdressers.add(hairdresser);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        this.clients.add(client);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HairdresserCompany that = (HairdresserCompany) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    /*
    @Override
    public String toString() {
        return "HairdresserCompany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hairdressers=" + hairdressers +
                ", clients=" + clients +
                ", services=" + services +
                ", rewards=" + rewards +
                '}';
    }
    */
}