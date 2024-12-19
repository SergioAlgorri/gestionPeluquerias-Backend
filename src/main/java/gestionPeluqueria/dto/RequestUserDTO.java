package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Role;

import java.time.LocalDate;
import java.util.Objects;

public class RequestUserDTO {

    private String name;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String telephone;
    private Role role;
    private long idHairdresser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getIdHairdresser() {
        return idHairdresser;
    }

    public void setIdHairdresser(long idHairdresser) {
        this.idHairdresser = idHairdresser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestUserDTO that = (RequestUserDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(firstSurname, that.firstSurname)
                && Objects.equals(secondSurname, that.secondSurname) && Objects.equals(email, that.email)
                && Objects.equals(password, that.password) && Objects.equals(birthDate, that.birthDate)
                && Objects.equals(telephone, that.telephone) && Objects.equals(idHairdresser, that.idHairdresser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, firstSurname, secondSurname, email, password, birthDate, telephone, idHairdresser);
    }
}
