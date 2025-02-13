package gestionPeluqueria.dto;
import gestionPeluqueria.entities.Role;

import java.time.LocalDate;

public class EmployeeDTO {

    private long id;
    private String fullName;
    private String email;
    private String telephone;
    private LocalDate birthDate;
    private Role role;

    public EmployeeDTO(long id, String name, String email, String telephone, LocalDate birthDate, Role role) {
        this.id = id;
        this.fullName = name;
        this.email = email;
        this.telephone = telephone;
        this.birthDate =  birthDate;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}