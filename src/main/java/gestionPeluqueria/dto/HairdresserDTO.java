package gestionPeluqueria.dto;

import java.time.LocalTime;
import java.util.List;

public class HairdresserDTO {

    private long id;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String address;
    private String telephone;
    private String companyName;
    private List<AppointmentDTO> appointments;
    private List<EmployeeDTO> employees;

    public HairdresserDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<AppointmentDTO> getAppointments() {
        return appointments;
    }

    public void addAppointments(AppointmentDTO a) {
        this.appointments.add(a);
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void addEmployees(EmployeeDTO e) {
        this.employees.add(e);
    }
}
