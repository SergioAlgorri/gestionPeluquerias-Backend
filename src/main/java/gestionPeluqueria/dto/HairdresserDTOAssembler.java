package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HairdresserDTOAssembler {

    public static HairdresserDTO generateDTO(Hairdresser hairdresser) {
        if (hairdresser == null) {
            return null;
        }

        HairdresserDTO result = getHairdresserDTO(hairdresser);

        for (Appointment a: hairdresser.getAppointments()) {
            LocalDateTime startTime = a.getStartTime();
            LocalDateTime endTime = a.getEndTime();
            String userName = concatFullName(a.getUser());
            String serviceName = a.getService().getName();
            String employeeName = concatFullName(a.getEmployee());
            BigDecimal price = a.getPrice();

            AppointmentDTO appointmentDTO = new AppointmentDTO(startTime, endTime, userName, serviceName,
                    employeeName, price);
            if (a.getReward() != null) {
                appointmentDTO.setRewardName(a.getReward().getName());
            }

            result.addAppointments(appointmentDTO);
        }

        for (Employee e: hairdresser.getEmployees()) {
            String name = concatFullName(e);
            String email = e.getEmail();
            EmployeeDTO employeeDTO = new EmployeeDTO(name, email);
            result.addEmployees(employeeDTO);
        }

        return result;
    }

    private static HairdresserDTO getHairdresserDTO(Hairdresser hairdresser) {
        HairdresserDTO result = new HairdresserDTO();

        // result.setId(hairdresser.getId());
        result.setOpeningTime(hairdresser.getOpeningTime());
        result.setClosingTime(hairdresser.getClosingTime());
        result.setAddress(hairdresser.getAddress());
        result.setTelephone(hairdresser.getTelephone());
        result.setCompanyName(hairdresser.getCompany().getName());
        return result;
    }

    private static String concatFullName(User u) {
        return u.getName() + " " + u.getFirstSurname() + " " + u.getSecondSurname();
    }
}
