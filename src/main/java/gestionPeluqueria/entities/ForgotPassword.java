package gestionPeluqueria.entities;

import gestionPeluqueria.entities.Inheritance.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "forgot_password")
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;

    /**
     * Empty constructor.
     */
    public ForgotPassword() {

    }

    /**
     * Parameterised constructor.
     * @param otp one-time password
     * @param expirationTime expiration time of the otp
     * @param user user who forgot password
     */
    public ForgotPassword(Integer otp, Date expirationTime, User user) {
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.user = user;
    }

    // Getters y Setter de los atributos de la clase

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}