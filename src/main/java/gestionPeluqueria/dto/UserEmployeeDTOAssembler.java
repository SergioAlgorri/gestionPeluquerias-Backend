package gestionPeluqueria.dto;

import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Inheritance.Employee;

public class UserEmployeeDTOAssembler {

    public static UserEmployeeDTO generateDTO(Employee employee) {
        UserEmployeeDTO result = new UserEmployeeDTO();

        result.setName(employee.getName());
        result.setFirstSurname(employee.getFirstSurname());
        result.setSecondSurname(employee.getSecondSurname());
        result.setEmail(employee.getEmail());
        result.setPassword(employee.getPassword());
        result.setBirthDate(employee.getBirthDate());
        result.setTelephone(employee.getTelephone());
        result.setRole(employee.getRole());

        HairdresserDTO hairdresserDTO = HairdresserDTOAssembler.generateDTO(employee.getHairdresser());
        result.setHairdresser(hairdresserDTO);

        for (Appointment a: employee.getActiveAppointments()) {
            AppointmentDTO appointmentDTO = new AppointmentDTO(a.getStartTime(), a.getEndTime(),
                    a.getService().getName(), a.getReward().getName(), a.getEmployee().getName(), a.getPrice());
            result.addAppointment(appointmentDTO);
        }

        return result;
    }
}
