package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.AppointmentDTO;
import gestionPeluqueria.dto.AppointmentDTOAssembler;
import gestionPeluqueria.dto.RequestAppointmentDTO;
import gestionPeluqueria.entities.Appointment;
import gestionPeluqueria.services.impl.AppointmentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AppointmentController {

    private final AppointmentServiceImpl appointmentService;

    public AppointmentController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping(value = "/peluquerias/{idPeluqueria}/citas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> findAll(@RequestParam(required = false) Long idEmployee,
                                                        @RequestParam(required = false) LocalDate date,
                                                        @PathVariable("idPeluqueria") long idHairdresser) {
        try {
            List<Appointment> appointments = appointmentService.findAll(idHairdresser, idEmployee, date);

            if (appointments == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<AppointmentDTO> result = appointments.stream()
                    .map(AppointmentDTOAssembler::generateDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "peluquerias/{idPeluqueria}/citas", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> createAppointment(@PathVariable("idPeluqueria") long idHairdresser,
                                                            @RequestBody RequestAppointmentDTO request) {
        try {
            Appointment appointmentCreated = appointmentService.createAppointment(idHairdresser, request);

            if (appointmentCreated == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AppointmentDTOAssembler.generateDTO(appointmentCreated));

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/peluquerias/{idPeluqueria}/citas/{idCita}")
    public ResponseEntity<HttpStatus> closeAppointment(@PathVariable("idPeluqueria") long idHairdresser,
                                                       @PathVariable("idCita") long idAppointment,
                                                       @RequestParam boolean hasReward) {
        try {
            appointmentService.closeAppointment(idHairdresser, idAppointment, hasReward);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/usuarios/{idUsuario}/citas")
    public ResponseEntity<List<AppointmentDTO>> getUserAppointments(@PathVariable("idUsuario") long idUser) {
        try {
            List<Appointment> appointments = appointmentService.findAllAppointmentsUser(idUser);

            if (appointments == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<AppointmentDTO> result = appointments.stream()
                    .map(AppointmentDTOAssembler::generateDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/usuarios/{idUsuario}/citas/{idCita}")
    public ResponseEntity<AppointmentDTO> getUserAppointment (@PathVariable("idUsuario") long idUser,
                                           @PathVariable("idCita") long idAppointment) {
        try {
            Appointment appointment = appointmentService.findById(idUser, idAppointment);

            if (appointment == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(AppointmentDTOAssembler.generateDTO(appointment), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/usuarios/{idUsuario}/historico_citas")
    public ResponseEntity<List<AppointmentDTO>> getHistoryAppointmentsUser(@PathVariable("idUsuario") long idUser) {
        try {
            List<Appointment> history = appointmentService.getHistoryAppointmentsUser(idUser);

            if (history == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<AppointmentDTO> result = history.stream()
                    .map(AppointmentDTOAssembler::generateDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "usuarios/{idUsuario}/citas/{idCita}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable("idUsuario") long idUser,
                                                            @PathVariable("idCita") long idAppointment,
                                                            @RequestBody Appointment appointment) {
        try {
            Appointment appointmentUpdated = appointmentService.updateAppointment(idUser, idAppointment, appointment);

            if (appointmentUpdated == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(AppointmentDTOAssembler.generateDTO(appointmentUpdated), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "usuarios/{idUsuario}/citas/{idCita}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("idUsuario") long idUser,
                                                        @PathVariable("idCita") long idAppointment) {
        try {
            appointmentService.deleteAppointment(idUser, idAppointment);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
