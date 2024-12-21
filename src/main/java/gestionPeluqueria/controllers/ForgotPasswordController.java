package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.ChangePasswordDTO;
import gestionPeluqueria.dto.MailBodyDTO;
import gestionPeluqueria.entities.ForgotPassword;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.repositories.ForgotPasswordRepository;
import gestionPeluqueria.repositories.UserRepository;
import gestionPeluqueria.services.impl.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/recuperar_contraseña")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService,
                                    ForgotPasswordRepository forgotPasswordRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Send mail for email verification
    @PostMapping(value = "/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email);

        Integer opt = optGenerator();
        MailBodyDTO mailBody = new MailBodyDTO(email, "Recuperar Contraseña",
                "El código de verificación es: " + opt);

        ForgotPassword fp = new ForgotPassword(opt, new Date(System.currentTimeMillis() + 90 * 1000), user);

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Correo electrónico enviado");
    }

    // Verify OTP
    @PostMapping(value = "{email}/verificar/{otp}")
    public ResponseEntity<String> verifyOtp(@PathVariable String email, @PathVariable Integer otp) {
        User user = userRepository.findByEmail(email);

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user);

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
           forgotPasswordRepository.delete(fp);
           return new ResponseEntity<>("Código Expirado", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("Código Verificado");
    }

    // Set new password
    @PostMapping(value = "/{email}/nueva_contraseña", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO request, @PathVariable String email) {
        if (!request.getNewPassword().equals(request.getRepeatNewPassword())) {
            return new ResponseEntity<>("Contraseñas diferentes", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Contraseña cambiada correctamente");
    }

    private Integer optGenerator() {
        Random random = new Random();
        int numDigits = random.nextInt(7) + 4;

        int min = (int) Math.pow(10, numDigits - 1);
        int max = (int) Math.pow(10, numDigits) - 1;

        // Generar el número aleatorio dentro del rango
        return random.nextInt(max - min + 1) + min;
    }
}