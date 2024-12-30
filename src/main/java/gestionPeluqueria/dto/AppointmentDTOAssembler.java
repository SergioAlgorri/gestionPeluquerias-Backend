package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;

public class AppointmentDTOAssembler {

    public static AppointmentDTO generateDTO(Appointment appointment) {
        String userName;
        if (appointment.getUser() != null) {
            userName = appointment.getUser().concatFullName();
        } else {
            userName = null;
        }

        String employeeName = appointment.getEmployee().concatFullName();
        AppointmentDTO result = new AppointmentDTO(
                appointment.getStartTime(), appointment.getEndTime(), appointment.getComment(), userName,
                appointment.getService().getName(), employeeName, appointment.getHairdresser().getAddress(),
                appointment.getPrice()
        );
        result.setId(appointment.getId());

        if (appointment.getReward() != null) {
            result.setRewardName(appointment.getReward().getName());
        }

        return result;
    }
}