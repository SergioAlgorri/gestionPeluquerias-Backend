package gestionPeluqueria.entities.Inheritance;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    @ManyToOne
    @JoinColumn(name = "hairdresser_id")
    private Hairdresser hairdresser;

    @OneToMany(mappedBy = "employee")
    private List<Appointment> activeAppointments = new ArrayList<>();

    public Employee() {
        this.setRole(Role.EMPLOYEE);
    }

    public Employee(String name, String firstSurname, String secondSurname, String email, String password,
                    LocalDate birthDate, String telephone) {
        super(name, firstSurname, secondSurname, email, password, birthDate, telephone);
        this.setRole(Role.EMPLOYEE);
    }

    public Hairdresser getHairdresser() {
        return hairdresser;
    }

    public void setHairdresser(Hairdresser hairdresser) {
        this.hairdresser = hairdresser;
    }

    public List<Appointment> getActiveAppointments() {
        return activeAppointments;
    }

    public void setActiveAppointments(List<Appointment> activeAppointments) {
        this.activeAppointments = activeAppointments;
    }
}
