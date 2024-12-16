package gestionPeluqueria.entities.Inheritance;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

    @Transient
    private int points;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();
    @OneToMany
    @JoinTable(name="appointments_history",
            joinColumns=@JoinColumn(name="client_id"),
            inverseJoinColumns=@JoinColumn(name="appointment_id"))
    private List<Appointment> history = new ArrayList<>();

    public Client() {
        // this.points = 0;
        this.setRole(Role.CLIENT);
    }

    public Client(String name, String firstSurname, String secondSurname, String email, String password,
                  LocalDate birthDate, String telephone) {
        super(name, firstSurname, secondSurname, email, password, birthDate, telephone);
        // this.points = 0;
        this.setRole(Role.CLIENT);
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void subtractPoint(int points) { this.points -= points; }

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
}
