package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class HairdresserDTOAssembler {

    public static HairdresserDTO generateDTO(Hairdresser hairdresser) {
        if (hairdresser == null) {
            return null;
        }

        HairdresserDTO result = getHairdresserDTO(hairdresser);

        for (Appointment a: hairdresser.getAppointments()) {
            if (a.getUser() != null) {
                LocalDateTime startTime = a.getStartTime();
                LocalDateTime endTime = a.getEndTime();
                String comment = a.getComment();
                String userName = a.getUser().concatFullName();
                String serviceName = a.getService().getName();
                String employeeName = a.getEmployee().concatFullName();
                String hairdresserAddress = a.getHairdresser().getAddress();
                BigDecimal price = a.getPrice();

                AppointmentDTO appointmentDTO = new AppointmentDTO(startTime, endTime, comment, userName, serviceName,
                        employeeName, hairdresserAddress, price);
                appointmentDTO.setId(a.getId());
                appointmentDTO.setIdClient(a.getUser().getId());
                if (a.getReward() != null) {
                    appointmentDTO.setRewardName(a.getReward().getName());
                }

                result.addAppointments(appointmentDTO);
            }
        }

        for (Employee e: hairdresser.getEmployees()) {
            long id = e.getId();
            String name = e.concatFullName();
            String email = e.getEmail();
            String telephone = e.getTelephone();
            LocalDate birthDate = e.getBirthDate();
            Role role = e.getRole();
            EmployeeDTO employeeDTO = new EmployeeDTO(id, name, email, telephone, birthDate, role);
            result.addEmployees(employeeDTO);
        }

        return result;
    }

    private static HairdresserDTO getHairdresserDTO(Hairdresser hairdresser) {
        HairdresserDTO result = new HairdresserDTO();

        result.setId(hairdresser.getId());
        result.setOpeningTime(hairdresser.getOpeningTime());
        result.setClosingTime(hairdresser.getClosingTime());
        result.setAddress(hairdresser.getAddress());
        result.setTelephone(hairdresser.getTelephone());
        result.setCompanyName(hairdresser.getCompany().getName());
        return result;
    }
}
