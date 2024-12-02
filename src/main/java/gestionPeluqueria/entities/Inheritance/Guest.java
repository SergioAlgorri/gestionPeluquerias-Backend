package gestionPeluqueria.entities.Inheritance;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("GUEST")
public class Guest extends User {

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    public Guest() {
        this.setRole(Role.GUEST);
    }

    public Guest(String name, String firstSurname, String secondSurname) {
        super(name, firstSurname, secondSurname, null, null, null, null);
        this.setRole(Role.GUEST);
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
