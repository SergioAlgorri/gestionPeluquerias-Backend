package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Inheritance.User;

public class AppointmentDTOAssembler {

    public static AppointmentDTO generateDTO(Appointment appointment) {
        String userName;
        if (appointment.getUser() != null) {
            userName = concatFullName(appointment.getUser());
        } else {
            userName = null;
        }

        String employeeName = concatFullName(appointment.getEmployee());
        AppointmentDTO result = new AppointmentDTO(
                appointment.getStartTime(), appointment.getEndTime(), appointment.getComment(), userName,
                appointment.getService().getName(), employeeName, appointment.getPrice()
        );

        if (appointment.getReward() != null) {
            result.setRewardName(appointment.getReward().getName());
        }

        return result;
    }

    private static String concatFullName(User u) {
        return u.getName() + " " + u.getFirstSurname() + " " + u.getSecondSurname();
    }
}
