package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.MailBodyDTO;
import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.repositories.AppointmentRepository;
import gestionPeluqueria.services.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailServiceImpl implements IEmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final AppointmentRepository appointmentRepository;

    public EmailServiceImpl(JavaMailSender javaMailSender, AppointmentRepository appointmentRepository) {
        this.javaMailSender = javaMailSender;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void sendSimpleMessage(MailBodyDTO mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.getTo());
        message.setFrom(from);
        message.setSubject(mailBody.getSubject());
        message.setText(mailBody.getText());

        javaMailSender.send(message);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void sendReminders() {
        LocalDateTime startReminder = LocalDateTime.now().plusHours(24);
        LocalDateTime endReminder = startReminder.plusMinutes(59);

        List<Appointment> appointments = appointmentRepository.findAppointmentsBetweenHours(startReminder, endReminder);

        for (Appointment a: appointments) {
            String email = a.getUser().getEmail();

            String userName = a.getUser().concatFullName();
            String startTime = a.getStartTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            String body = String.format("Hola %s,\n\nEste es un recordatorio de tu cita programada para el %s." +
                    "\n\n¡Te esperamos!", userName, startTime);

            MailBodyDTO mailBody = new MailBodyDTO(email, "Recordatorio Cita", body);
            sendSimpleMessage(mailBody);
        }
    }
}