package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.RequestAppointmentDTO;
import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.Guest;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Reward;
import gestionPeluqueria.entities.Role;
import gestionPeluqueria.entities.composite.ServiceComponent;
import gestionPeluqueria.repositories.*;
import gestionPeluqueria.services.IAppointmentHairdresserService;
import gestionPeluqueria.services.IAppointmentUserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements IAppointmentHairdresserService, IAppointmentUserService {

    private final AppointmentRepository appointmentRepository;
    private final HairdresserRepository hairdresserRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final RewardRepository rewardRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  HairdresserRepository hairdresserRepository, UserRepository userRepository,
                                  ServiceRepository serviceRepository, RewardRepository rewardRepository) {
        this.appointmentRepository = appointmentRepository;
        this.hairdresserRepository = hairdresserRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.rewardRepository = rewardRepository;
    }

    // Interfaz IAppointmentHairdresserService

    @Override
    public List<Appointment> findAll(long idHairdresser, Long idEmployee, LocalDate date) {
        Hairdresser hairdresser = hairdresserRepository.findById(idHairdresser);
        if (hairdresser == null) {
            return null;
        }

        if (date == null) {
            date = LocalDate.now();
        }

        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(23, 59, 59);

        if (idEmployee != null) {
            User employee = userRepository.findById(idEmployee.longValue());
            if (!(employee instanceof Employee)) {
                return null;
            } else {
                for (Employee e: hairdresser.getEmployees()) {
                    if (e.equals(employee)) {
                        return appointmentRepository.findByHairdresserAndEmployeeId(idHairdresser, idEmployee,
                                startDay, endDay);
                    }
                }
            }
        }

        return appointmentRepository.findByHairdresser(hairdresser.getId(), startDay, endDay);
    }

    @Override
    public List<Appointment> getHistoryAppointmentsHairdresser(long idHairdresser) {

        if (hairdresserRepository.findById(idHairdresser) == null) {
            return null;
        }

        return appointmentRepository.findByHairdresserIdAndAttendedTrue(idHairdresser);
    }

    @Override
    @Transactional
    public Appointment createAppointment(long idHairdresser, RequestAppointmentDTO request) {
        // Validar IDs
        Hairdresser hairdresser = hairdresserRepository.findById(idHairdresser);
        if (hairdresser == null) {
            return null;
        }

        ServiceComponent service = serviceRepository.findById(request.getIdService());
        if (service == null) {
            return null;
        }

        User user;
        if (request.getIdUser() != null) {
            user = userRepository.findById(request.getIdUser().longValue());
            if (user == null) {
                return null;
            }
        } else if (request.getName() != null && request.getFirstSurname() != null && request.getSecondSurname() != null
                && !request.getName().isEmpty() && !request.getFirstSurname().isEmpty()) {
            user = new Guest(request.getName(), request.getFirstSurname(), request.getSecondSurname());
            userRepository.save(user);
        } else {
            return null;
        }

        // Check Availability
        LocalDateTime endTime = request.getStartTime().plusMinutes(service.getTotalDuration());
        if (request.getStartTime().toLocalTime().isBefore(hairdresser.getOpeningTime()) ||
                endTime.toLocalTime().isAfter(hairdresser.getClosingTime()) ||
                !checkAvailability(hairdresser, request.getStartTime(), endTime)) {
            return null;
        }

        // Asignar empleado
        Employee employee;
        if (request.getIdEmployee() != null) {
            User employeeSelected = userRepository.findById(request.getIdEmployee().longValue());
            if (employeeSelected == null) {
                return null;
            }
            // Check employee availability
            if (!checkEmployeeAvailability(idHairdresser, (Employee) employeeSelected,
                    request.getStartTime(), endTime)) {
                return null;
            }

            employee = (Employee) employeeSelected;
        } else {
            employee = assignEmployee(hairdresser, request.getStartTime(), endTime);
        }

        // Comprobar Recompensa
        Reward reward = null;
        if (request.getIdReward() != null && user instanceof Client) {
            reward = rewardRepository.findById(request.getIdReward()).orElse(null);
        }

        // Crear cita
        Appointment appointment = new Appointment(request.getStartTime(), null, user, employee, service,
                reward, hairdresser);

        // Comprobar si el usuario ya tiene una cita a la misma hora o intenta crear una cita durante otra de sus citas
        if (user instanceof Client) {
            for (Appointment a: ((Client) user).getAppointments()) {
                if (a.equals(appointment) || (appointment.getStartTime().isAfter(a.getStartTime())
                        && appointment.getStartTime().isBefore(a.getEndTime()))) {
                    return null;
                }
            }
        } else {
            ((Guest) user).setAppointment(appointment);
            userRepository.save(user);
        }

        appointmentRepository.save(appointment);
        hairdresser.addAppointments(appointment);
        hairdresserRepository.save(hairdresser);
        return appointment;
    }

    @Override
    @Transactional
    public void closeAppointment(long idHairdresser, long idAppointment, boolean hasReward) {
        // Validate IDs
        Hairdresser hairdresser = hairdresserRepository.findById(idHairdresser);
        Appointment appointment = appointmentRepository.findById(idAppointment);

        if (hairdresser == null || appointment == null) {
            throw new IllegalArgumentException();
        }

        if (!hairdresser.getAppointments().contains(appointment)) {
            throw new IllegalArgumentException();
        }

        User user = appointment.getUser();
        Employee employee = appointment.getEmployee();

        // El usuario de la cita es un CLIENT
        if (user instanceof Client) {
            // La cita ya ha sido cerrada (está en el histórico del usuario)
            if (((Client) user).getHistory().contains(appointment)) {
                throw new IllegalArgumentException();
            }

            // Gestión de los puntos y la recompensa
            if (appointment.getReward() != null && hasReward) {
                int userPoints = ((Client) user).getPoints();
                int rewardPoints = appointment.getReward().getPoints();

                if (userPoints >= rewardPoints) {
                    ((Client) user).subtractPoint(rewardPoints);
                } else {
                    // En teoria el flujo no tendría que pasar por esta excepción ya que el cliente solo
                    // puede elegir recompensas disponibles respecto a sus puntos actuales
                    throw new RuntimeException();
                }
            } else {
                // Se cancela la recompensa si estuviera marcada (actualizandose el precio y los puntos automaticamente)
                appointment.setReward(null);
                ((Client) user).addPoints(appointment.getPoints());
            }

            // Eliminar de las citas activas del cliente, del empleado y de la peluqueria
            // Al eliminar de las citas activas del cliente automaticamente pasa a su historico
            ((Client) user).addHistory(appointment);
            appointment.setUser(null);
            appointment.setAttended(true);
            employee.getActiveAppointments().remove(appointment);
            hairdresser.getAppointments().remove(appointment);

            userRepository.save(user);
            userRepository.save(employee);
            hairdresserRepository.save(hairdresser);
        } else {
            // El usuario de la cita es un GUEST
            appointmentRepository.delete(appointment);
            userRepository.delete(user);
        }
    }

    // Interfaz IAppointmentUserService

    @Override
    public List<Appointment> findAllAppointmentsUser(long idUser) {
        User user = userRepository.findById(idUser);

        if (user == null) {
            return null;
        }

        return appointmentRepository.findByUser(user.getId());
    }

    @Override
    public Appointment findById(long idUser, long idAppointment) {
        User user = userRepository.findById(idUser);
        if (user == null) {
            return null;
        }

        Appointment appointment = appointmentRepository.findById(idAppointment);
        if (appointment == null) {
            return null;
        }

        if (!((Client) user).getAppointments().contains(appointment)) {
            return null;
        }

        return appointment;
    }

    @Override
    public Appointment updateAppointment(long idUser, long idAppointment, Appointment appointment) {
        // Validate IDs
        User user = userRepository.findById(idUser);
        if (user == null) {
            return null;
        }

        Appointment appointmentFound = appointmentRepository.findById(idAppointment);
        if (appointmentFound == null) {
            return null;
        }

        if (!((Client) user).getAppointments().contains(appointmentFound)) {
            return null;
        }

        // Update only the comment by an employee. If a client want to change the date or the service or the reward,
        // delete the current appointment and create another one.
        if (appointment.getComment() != null) {
            appointmentFound.setComment(appointment.getComment());
        }

        return appointmentRepository.save(appointmentFound);
    }

    @Override
    public void deleteAppointment(long idUser, long idAppointment) {
        Appointment appointment = appointmentRepository.findById(idAppointment);
        User user = userRepository.findById(idUser);

        if (user == null || appointment == null) {
            throw new IllegalArgumentException();
        }

        if (user.getRole().equals(Role.CLIENT)) {
            for (Appointment a: ((Client) user).getAppointments()) {
                if (a.equals(appointment)) {
                    ((Client) user).getAppointments().remove(a);
                    userRepository.save(user);
                    appointmentRepository.delete(a);
                    return;
                }
            }
        } else {
            ((Guest) user).setAppointment(null);
            userRepository.save(user);
            appointmentRepository.delete(appointment);
        }

        throw new IllegalArgumentException();
    }

    @Override
    public List<Appointment> getHistoryAppointmentsUser(long idUser) {
        User user = userRepository.findById(idUser);
        if (user == null) {
            return null;
        }

        List<Appointment> historyAppointments = new ArrayList<>();
        if (user instanceof Client) {
            for (Appointment a :((Client) user).getHistory()) {
                a.setUser(user);
                historyAppointments.add(a);
            }
        } else {
            return null;
        }

        return historyAppointments;
    }

    public List<LocalTime> getAvailability(long idHairdresser, long idService, LocalDate date, Long idEmployee) {
        // Hairdresser
        Hairdresser hairdresser = hairdresserRepository.findById(idHairdresser);

        // Service
        ServiceComponent service = serviceRepository.findById(idService);

        if (hairdresser == null || service == null) {
            return null;
        }

        // Employee
        User employee = null;
        if (idEmployee != null) {
            employee = userRepository.findById(idEmployee.longValue());
        }

        int totalDurationService = service.getTotalDuration();
        LocalTime openingTime = hairdresser.getOpeningTime();
        LocalTime closingTime = hairdresser.getClosingTime();

        LocalTime startHour = openingTime;
        if (date.equals(LocalDate.now())) {
            LocalTime currentHour = LocalTime.now();
            if (currentHour.isAfter(openingTime) && currentHour.isBefore(closingTime)) {
                startHour = currentHour.plusMinutes(15 - (currentHour.getMinute() % 15));
            }
        }

        LocalDateTime startDay = date.atTime(startHour);
        LocalDateTime endDay = date.atTime(closingTime);

        List<LocalTime> availableHours = new ArrayList<>();
        if (endDay.isBefore(startDay)) {
            return availableHours;
        }
        LocalDateTime iterHour = startDay;
        while (iterHour.plusMinutes(totalDurationService).isBefore(endDay) ||
                iterHour.plusMinutes(totalDurationService).equals(endDay)) {
            LocalDateTime endTimeService = iterHour.plusMinutes(totalDurationService);

            boolean hairdresserAvailable = checkAvailability(hairdresser, iterHour, endTimeService);

            boolean employeeAvailable = true;
            if (employee != null) {
                employeeAvailable = checkEmployeeAvailability(idHairdresser, (Employee) employee,
                        iterHour, endTimeService);
            }

            if (hairdresserAvailable && employeeAvailable) {
                availableHours.add(iterHour.toLocalTime());
            }

            iterHour = iterHour.plusMinutes(15);
        }

        return availableHours;
    }

    // Métodos Auxiliares

    // Public para el test
    public boolean checkAvailability(Hairdresser hairdresser, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime startDay = startTime.toLocalDate().atStartOfDay();
        LocalDateTime endDay = endTime.toLocalDate().atTime(23, 59, 59);
        List<Appointment> existingAppointments =
                appointmentRepository.findByHairdresser(hairdresser.getId(), startDay, endDay);

        for (Appointment a: existingAppointments) {
            LocalDateTime existingStartTime = a.getStartTime();
            LocalDateTime existingEndTime = a.getEndTime();

            // Comprobar conflicto
            if (startTime.isBefore(existingEndTime) && existingStartTime.isBefore(endTime)) {
                for (Employee e: hairdresser.getEmployees()) {
                    if (checkEmployeeAvailability(hairdresser.getId(), e, startTime, endTime)) {
                        return true;
                    }
                }

                return false;
            }
        }

        return true;
    }

    // Public para el test
    public boolean checkEmployeeAvailability(long idHairdresser, Employee employee, LocalDateTime startTime,
                                              LocalDateTime endTime) {
        LocalDateTime startDay = startTime.toLocalDate().atStartOfDay();
        LocalDateTime endDay = endTime.toLocalDate().atTime(23, 59, 59);
        List<Appointment> employeeAppointments =
                appointmentRepository.findByHairdresserAndEmployeeId(idHairdresser, employee.getId(), startDay, endDay);

        for (Appointment a: employeeAppointments) {
            // Comprobar conflicto
            if (startTime.isBefore(a.getEndTime()) && a.getStartTime().isBefore(endTime)) {
                // Comprobar si la cita que da conflicto tiene descanso suficiente para la cita que se crea
                // Si el array de duraciones es de más de un elemento, la cita tiene al menos un descanso
                if (a.getService().getDuration().size() > 1) {
                    LocalDateTime startRestTime = a.getStartTime().plusMinutes(a.getService().getDuration().get(0));
                    LocalDateTime endRestTime = startRestTime.plusMinutes(a.getService().getDuration().get(1));

                    if (startRestTime.isBefore(startTime) || startRestTime.isEqual(startTime)) {
                        if (endRestTime.isAfter(endTime) || endRestTime.isEqual(endTime)) {
                            // La nueva cita puede hacerse en el tiempo de descanso de la cita
                            return true;
                        }
                    } else {
                        // La nueva cita no puede hacerse en el tiempo de descanso de la cita
                        return false;
                    }
                }
                // La cita no tiene descansos, por lo que hay conflicto
                return false;
            }
        }
        // No hay conflictos, el empleado está disponible
        return true;
    }

    private Employee assignEmployee(Hairdresser hairdresser, LocalDateTime startTime, LocalDateTime endTime) {
        List<Employee> employees = hairdresser.getEmployees();
        Employee employeeSelected = null;
        int minimumWork = Integer.MAX_VALUE;
        LocalDateTime startDay = startTime.toLocalDate().atStartOfDay();
        LocalDateTime endDay = endTime.toLocalDate().atTime(23, 59, 59);

        for (Employee e: employees) {
            if (checkEmployeeAvailability(hairdresser.getId(), e, startTime, endTime)) {
                int numAppointments = appointmentRepository.countByEmployeeAndDate(e.getId(), startDay, endDay);
                if (numAppointments < minimumWork) {
                    minimumWork = numAppointments;
                    employeeSelected = e;
                }
            }
        }

        return employeeSelected;
    }

    // Se ejecuta una vez al día a las 00:00:00
    @Scheduled(cron = "59 59 23 * * MON-SUN")
    public void deleteExpiredAppointments() {
        LocalDateTime endTime = LocalDateTime.now();
        List<Appointment> expiredAppointments = appointmentRepository.findByStartTimeBeforeAndAttendedFalse(endTime);

        if (!expiredAppointments.isEmpty()) {
            appointmentRepository.deleteAll(expiredAppointments);
        }
    }
}
