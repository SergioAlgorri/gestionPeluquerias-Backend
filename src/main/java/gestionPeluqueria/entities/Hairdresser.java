package gestionPeluqueria.entities;

import gestionPeluqueria.entities.Inheritance.Employee;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "hairdressers")
public class Hairdresser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    private String address;

    private String telephone;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private HairdresserCompany company;

    @OneToMany(mappedBy = "hairdresser")
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "hairdresser")
    private List<Appointment> appointments = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public Hairdresser() {

    }

    /**
     * Parameterised constructor.
     * @param openingTime opening time of the hairdresser.
     * @param closingTime closing time of the hairdresser.
     * @param address address of the hairdresser.
     * @param telephone telephone of the hairdresser.
     */
    public Hairdresser(LocalTime openingTime, LocalTime closingTime, String address, String telephone) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.address = address;
        this.telephone = telephone;
    }

    // Getters y Setter de los atributos de la clase

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

    public HairdresserCompany getCompany() {
        return company;
    }

    public void setCompany(HairdresserCompany company) {
        this.company = company;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointments(Appointment appointments) {
        this.appointments.add(appointments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hairdresser that = (Hairdresser) o;
        return Objects.equals(openingTime, that.openingTime) && Objects.equals(closingTime, that.closingTime)
                && Objects.equals(address, that.address) && Objects.equals(telephone, that.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openingTime, closingTime, address, telephone);
    }

    /*
    @Override
    public String toString() {
        return "Hairdresser{" +
                "id=" + id +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", company=" + company +
                ", employees=" + employees +
                ", appointments=" + appointments +
                '}';
    }
     */
}