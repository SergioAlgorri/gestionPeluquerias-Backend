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

    private static final double POINTS_MULTIPLIER = 0.1; // 10%

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

    @Transient
    private int points;

    // Empty constructor
    public Appointment() {

    }

    public Appointment(LocalDateTime startTime, String comment, User user, Employee employee,
                       ServiceComponent service, Reward reward, Hairdresser hairdresser) {
        this.startTime = startTime;
        this.comment = comment;
        this.user = user;
        this.employee = employee;
        this.service = service;
        this.reward = reward;
        this.hairdresser = hairdresser;

        this.endTime = calculateEndingTime(startTime);
        this.price = calculatePrice();
        this.points = calculatePoints();
    }

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

    public LocalDateTime calculateEndingTime(LocalDateTime endingTime) {
       return this.endTime = startTime.plusMinutes(service.getTotalDuration());
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal calculatePrice() {
        if (reward != null) {
            return service.getPrice().multiply(reward.getDiscountAmount()).setScale(2, RoundingMode.DOWN);
        }

        return service.getPrice().setScale(2, RoundingMode.DOWN);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPoints() {
        return points;
    }

    public int calculatePoints() {
        if (reward == null) {
            double points = this.service.getPrice().doubleValue() * POINTS_MULTIPLIER;
            return this.points = (int) points;
        }

        return 0;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(comment, that.comment)
                && Objects.equals(user, that.user) && Objects.equals(service, that.service)
                && Objects.equals(reward, that.reward) && Objects.equals(hairdresser, that.hairdresser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, comment, user, service, reward, hairdresser);
    }

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
}
