package gestionPeluqueria.services;

import gestionPeluqueria.entities.Appointment;

import java.util.List;

public interface IAppointmentUserService {

    List<Appointment> findAllAppointmentsUser(long idUser);
    Appointment findById(long idUser, long idAppointment);
    Appointment updateAppointment(long idUser, long idAppointment, Appointment appointment);
    void deleteAppointment(long idUser, long idAppointment);
    List<Appointment> getHistoryAppointmentsUser(long idUser);
}
