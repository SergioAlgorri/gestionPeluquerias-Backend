package gestionPeluqueria.entities.Inheritance;

import gestionPeluqueria.entities.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    public Admin() {
        this.setRole(Role.ADMIN);
    }

    public Admin(String name, String firstSurname, String secondSurname, String email, String password,
                 LocalDate birthDate, String telephone) {
        super(name, firstSurname, secondSurname, email, password, birthDate, telephone);
        this.setRole(Role.ADMIN);
    }
}
