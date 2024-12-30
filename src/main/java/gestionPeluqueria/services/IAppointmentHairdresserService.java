package gestionPeluqueria.services;

import gestionPeluqueria.dto.RequestAppointmentDTO;
import gestionPeluqueria.entities.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentHairdresserService {

    List<Appointment> findAll(long idHairdresser, Long idEmployee, LocalDate date);
    Appointment createAppointment(long idHairdresser, RequestAppointmentDTO appointment);
    void closeAppointment(long idHairdresser, long idAppointment, boolean hasReward);
}