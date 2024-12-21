package gestionPeluqueria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEmployeeDTO {

    private Role role;
    private String fullName;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String telephone;
    private Integer points;
    private List<AppointmentDTO> appointments = new ArrayList<>();
    private List<AppointmentDTO> history = new ArrayList<>();
    private HairdresserDTO hairdresser;

    public UserEmployeeDTO() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
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

    public List<AppointmentDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentDTO> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(AppointmentDTO appointmentDTO) {
        this.appointments.add(appointmentDTO);
    }

    public List<AppointmentDTO> getHistory() {
        return history;
    }

    public void setHistory(List<AppointmentDTO> appointments) {
        this.history = appointments;
    }

    public void addHistoryAppointment(AppointmentDTO appointmentDTO) {
        this.history.add(appointmentDTO);
    }
}