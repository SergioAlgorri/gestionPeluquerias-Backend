package gestionPeluqueria.entities.Inheritance;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Role;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User implements Serializable {

    private int points;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany
    @JoinTable(name="appointments_history",
            joinColumns=@JoinColumn(name="client_id"),
            inverseJoinColumns=@JoinColumn(name="appointment_id"))
    private List<Appointment> history = new ArrayList<>();

    /**
     * Empty constrcutor.
     */
    public Client() {
        this.points = 0;
        this.setRole(Role.CLIENT);
    }

    /**
     * Parameterised constructor.
     * Same constructor of the parent class by adding the CLIENT role and the points to it.
     */
    public Client(String name, String firstSurname, String secondSurname, String email, String password,
                  LocalDate birthDate, String telephone) {
        super(name, firstSurname, secondSurname, email, password, birthDate, telephone);
        this.points = 0;
        this.setRole(Role.CLIENT);
    }

    // Getters y Setter de los atributos de la clase

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Appointment> getHistory() {
        return history;
    }

    public void addHistory(Appointment appointment) {
        appointments.remove(appointment);
        history.add(appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Method that adds up points for the client.
     * @param points points to be added.
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Method for subtracting points from the client.
     * @param points points to be subtracted.
     */
    public void subtractPoint(int points) { this.points -= points; }
}