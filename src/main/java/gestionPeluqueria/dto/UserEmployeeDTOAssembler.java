package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Inheritance.Admin;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;

public class UserEmployeeDTOAssembler {

    public static UserEmployeeDTO generateDTO(User user) {
        UserEmployeeDTO result = new UserEmployeeDTO();

        result.setFullName(concatFullName(user));
        result.setEmail(user.getEmail());
        result.setPassword(user.getPassword());
        result.setBirthDate(user.getBirthDate());
        result.setTelephone(user.getTelephone());
        result.setRole(user.getRole());

        String userName = concatFullName(user);
        String employeeName = concatFullName(user);

        if (user instanceof Client) {
            result.setHairdresser(null);
            result.setPoints(((Client) user).getPoints());
            for (Appointment a: ((Client) user).getAppointments()) {
                // Cita actual. Si user fuese nulo significa que está en el historial
                if (a.getUser() != null) {
                    employeeName = concatFullName(a.getEmployee());
                    AppointmentDTO appointmentDTO = new AppointmentDTO(a.getStartTime(), a.getEndTime(), a.getComment(),
                            userName, a.getService().getName(), employeeName, a.getPrice());
                    if (a.getReward() != null) {
                        appointmentDTO.setRewardName(a.getReward().getName());
                    }

                    result.addAppointment(appointmentDTO);
                }
            }

            for (Appointment a: ((Client) user).getHistory()) {
                employeeName = concatFullName(a.getEmployee());
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
                // Cita actual. Si user fuese nulo significa que está en el historial
                if (a.getUser() != null) {
                    userName = concatFullName(a.getUser());
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

    private static String concatFullName(User u) {
        return u.getName() + " " + u.getFirstSurname() + " " + u.getSecondSurname();
    }
}
