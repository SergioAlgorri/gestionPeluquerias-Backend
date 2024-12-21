package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.ForgotPassword;
import gestionPeluqueria.entities.Inheritance.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

    ForgotPassword findByOtpAndUser(Integer otp, User user);
}
