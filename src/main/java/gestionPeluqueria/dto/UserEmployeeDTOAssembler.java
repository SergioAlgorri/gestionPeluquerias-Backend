package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Inheritance.Admin;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;

public class UserEmployeeDTOAssembler {

    public static UserEmployeeDTO generateDTO(User user) {
        UserEmployeeDTO result = new UserEmployeeDTO();

        result.setFullName(user.concatFullName());
        result.setEmail(user.getEmail());
        result.setPassword(user.getPassword());
        result.setBirthDate(user.getBirthDate());
        result.setTelephone(user.getTelephone());
        result.setRole(user.getRole());

        String userName = user.concatFullName();
        String employeeName = user.concatFullName();

        if (user instanceof Client) {
            result.setHairdresser(null);
            result.setPoints(((Client) user).getPoints());
            for (Appointment a: ((Client) user).getAppointments()) {
                // Cita actual. Si user fuese nulo significa que está en el historial
                if (a.getUser() != null) {
                    employeeName = a.getEmployee().concatFullName();
                    AppointmentDTO appointmentDTO = new AppointmentDTO(a.getStartTime(), a.getEndTime(), a.getComment(),
                            userName, a.getService().getName(), employeeName, a.getPrice());
                    if (a.getReward() != null) {
                        appointmentDTO.setRewardName(a.getReward().getName());
                    }

                    result.addAppointment(appointmentDTO);
                }
            }

            for (Appointment a: ((Client) user).getHistory()) {
                employeeName = a.getEmployee().concatFullName();
                AppointmentDTO appointmentDTO = new AppointmentDTO(a.getStartTime(), a.getEndTime(), a.getComment(),
                        userName, a.getService().getName(), employeeName, a.getPrice());
                if (a.getReward() != null) {
                    appointmentDTO.setRewardName(a.getReward().getName());
                }

                result.addHistoryAppointment(appointmentDTO);
            }
        } else if (user instanceof Employee) {
            HairdresserDTO hairdresserDTO = HairdresserDTOAssembler.generateDTO(((Employee) user).getHairdresser());
            hairdresserDTO.setEmployees(null);
            hairdresserDTO.setAppointments(null);
            result.setHairdresser(hairdresserDTO);
            result.setHistory(null);
            result.setPoints(null);
            for (Appointment a: ((Employee) user).getActiveAppointments()) {
                // Cita actual. Si USER fuese nulo significa que está en el historial
                if (a.getUser() != null) {
                    userName = a.getUser().concatFullName();
                    AppointmentDTO appointmentDTO = new AppointmentDTO(a.getStartTime(), a.getEndTime(), a.getComment(),
                            userName, a.getService().getName(), employeeName, a.getPrice());
                    if (a.getReward() != null) {
                        appointmentDTO.setRewardName(a.getReward().getName());
                    }

                    result.addAppointment(appointmentDTO);
                }
            }
        } else {
            result.setHistory(null);
            result.setAppointments(null);
            return result;
        }

        return result;
    }
}
