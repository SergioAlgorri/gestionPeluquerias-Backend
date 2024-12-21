package gestionPeluqueria.entities.Inheritance;

import gestionPeluqueria.entities.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    /**
     * Empty constructor.
     */
    public Admin() {
        this.setRole(Role.ADMIN);
    }

    /**
     * Parameterised constructor.
     * Same constructor of the parent class by adding the ADMIN role to it.
     */
    public Admin(String name, String firstSurname, String secondSurname, String email, String password,
                 LocalDate birthDate, String telephone) {
        super(name, firstSurname, secondSurname, email, password, birthDate, telephone);
        this.setRole(Role.ADMIN);
    }
}