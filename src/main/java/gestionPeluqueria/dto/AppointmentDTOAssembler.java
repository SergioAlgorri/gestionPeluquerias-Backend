package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;

public class AppointmentDTOAssembler {

    public static AppointmentDTO generateDTO(Appointment appointment) {
        /*
        String userName;
        Long idClient;
        if (appointment.getUser() != null) {
            userName = appointment.getUser().concatFullName();
            idClient = appointment.getUser().getId();
        } else {
            userName = null;
            idClient = null;
        }
         */

        String employeeName = appointment.getEmployee().concatFullName();
        AppointmentDTO result = new AppointmentDTO(
                appointment.getStartTime(), appointment.getEndTime(), appointment.getComment(),
                appointment.getUser().concatFullName(), appointment.getService().getName(),
                employeeName, appointment.getHairdresser().getAddress(), appointment.getPrice()
        );
        result.setId(appointment.getId());
        result.setIdClient(appointment.getUser().getId());

        if (appointment.getReward() != null) {
            result.setRewardName(appointment.getReward().getName());
        }

        return result;
    }
}