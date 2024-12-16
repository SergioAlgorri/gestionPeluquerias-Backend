package gestionPeluqueria.services;

import gestionPeluqueria.dto.RequestAppointmentDTO;
import gestionPeluqueria.entities.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentService {

    List<Appointment> findAll(long idHairdresser, Long idEmployee, LocalDate date);
    List<Appointment> findAllAppointmentsUser(long idUser);
    Appointment finById(long idUser, long idAppointment);
    Appointment createAppointment(long idHairdresser, RequestAppointmentDTO appointment);
    Appointment updateAppointment(long idUser, long idAppointment, Appointment appointment);
    void deleteAppointment(long idUser, long idAppointment);
    List<Appointment> getHistoryAppointmentsUser(long idUser);
    void closeAppointment(long idHairdresser, long idAppointment, boolean hasReward);
}
