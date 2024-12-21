package gestionPeluqueria.entities;

import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.composite.ServiceComponent;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "appointments")
public class Appointment {

    // Factor multiplicador de puntos por cada reserva (10%)
    private static final double POINTS_MULTIPLIER = 0.1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private BigDecimal price;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceComponent service;

    @ManyToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @ManyToOne
    @JoinColumn(name = "hairdresser_id")
    private Hairdresser hairdresser;

    private boolean attended;

    @Transient
    private int points;

    /**
     * Empty constructor.
     */
    public Appointment() {
        this.attended = false;
    }

    /**
     * Parameterised constructor.
     * @param startTime start time of the appointment.
     * @param comment comment of the appointment.
     * @param user user of the appointment.
     * @param employee empployee of the appointment.
     * @param service service of the appointment.
     * @param reward reward of the appointment.
     * @param hairdresser hairdresser of the appointment.
     */
    public Appointment(LocalDateTime startTime, String comment, User user, Employee employee,
                       ServiceComponent service, Reward reward, Hairdresser hairdresser) {
        this.startTime = startTime;
        this.comment = comment;
        this.user = user;
        this.employee = employee;
        this.service = service;
        this.reward = reward;
        this.hairdresser = hairdresser;

        this.endTime = calculateEndingTime();
        this.price = calculatePrice();
        this.points = calculatePoints();
        this.attended = false;
    }

    // Getters y Setter de los atributos de la clase

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public BigDecimal getPrice() {
        return calculatePrice();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPoints() {
        return calculatePoints();
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
        this.calculatePoints();
        this.calculatePrice();
    }

    public ServiceComponent getService() {
        return service;
    }

    public void setService(ServiceComponent service) {
        this.service = service;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hairdresser getHairdresser() {
        return hairdresser;
    }

    public void setHairdresser(Hairdresser hairdresser) {
        this.hairdresser = hairdresser;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    /**
     * Method that calculates the end time of an appointment.
     * @return end time of the appointment.
     */
    public LocalDateTime calculateEndingTime() {
        return this.endTime = startTime.plusMinutes(service.getTotalDuration());
    }

    /**
     * Method that calculates the price of an appointment
     * @return price of the appointment
     */
    public BigDecimal calculatePrice() {
        if (reward != null) {
            return service.getPrice().multiply(reward.getDiscountAmount()).setScale(2, RoundingMode.DOWN);
        }

        return service.getPrice().setScale(2, RoundingMode.DOWN);
    }

    /**
     * Method for calculating the appointment points that the client can earn.
     * @return points that the client can earn.
     */
    public int calculatePoints() {
        if (reward == null) {
            this.points = (int) Math.floor(this.getPrice().intValue() * POINTS_MULTIPLIER);
            return this.points;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, user);
    }

    /*
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", price=" + price +
                ", comment='" + comment + '\'' +
                ", points=" + points +
                ", client=" + user +
                ", employee=" + employee +
                ", service=" + service +
                ", reward=" + reward +
                ", hairdresser=" + hairdresser +
                '}';
    }
     */
}