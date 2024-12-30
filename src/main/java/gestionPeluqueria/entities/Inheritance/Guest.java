package gestionPeluqueria.entities.Inheritance;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.io.Serializable;

@Entity
@DiscriminatorValue("GUEST")
public class Guest extends User implements Serializable {

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    /**
     * Empty constructor.
     */
    public Guest() {
        this.setRole(Role.GUEST);
    }

    /**
     * Parameterised constructor.
     * Same constructor of the parent class (only name, firstSurname and secondSurname) by adding the GUEST role to it.
     */
    public Guest(String name, String firstSurname, String secondSurname) {
        super(name, firstSurname, secondSurname, null, null, null, null);
        this.setRole(Role.GUEST);
    }

    // Getters y Setter de los atributos de la clase

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}