package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Role;

import java.time.LocalDate;
import java.util.List;

public class UserEmployeeDTO {

    private String name;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String telephone;
    private Role role;
    private HairdresserDTO hairdresser;
    private List<AppointmentDTO> appointments;

    public UserEmployeeDTO() {
    }

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

    public HairdresserDTO getHairdresser() {
        return hairdresser;
    }

    public void setHairdresser(HairdresserDTO hairdresser) {
        this.hairdresser = hairdresser;
    }

    public void addAppointment(AppointmentDTO appointmentDTO) {
        this.appointments.add(appointmentDTO);
    }
}
