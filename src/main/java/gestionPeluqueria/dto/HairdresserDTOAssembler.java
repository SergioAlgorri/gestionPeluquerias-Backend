package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.composite.ServiceComponent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HairdresserDTOAssembler {

    public static HairdresserDTO generateDTO(Hairdresser hairdresser) {
        HairdresserDTO result = getHairdresserDTO(hairdresser);

        for (Appointment a: hairdresser.getAppointments()) {
            LocalDateTime startTime = a.getStartTime();
            LocalDateTime endTime = a.getEndTime();
            String serviceName = a.getService().getName();
            String rewardName = a.getReward().getName();
            String employeeName = a.getEmployee().getName();
            BigDecimal price = a.getPrice();

            AppointmentDTO appointmentDTO = new AppointmentDTO(startTime, endTime, serviceName, rewardName,
                    employeeName, price);
            result.addAppointments(appointmentDTO);
        }

        for (Employee e: hairdresser.getEmployees()) {
            String name = e.getName();
            String email = e.getEmail();
            EmployeeDTO employeeDTO = new EmployeeDTO(name, email);
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
