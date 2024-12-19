package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.hairdresser.id = :idHairdresser " +
            "AND a.startTime >= :startDay AND a.startTime <= :endDay ORDER BY a.startTime ASC")
    List<Appointment> findByHairdresser(long idHairdresser, LocalDateTime startDay, LocalDateTime endDay);
    @Query("SELECT a FROM Appointment a WHERE a.hairdresser.id = :idHairdresser " +
            "AND a.employee.id = :idEmployee AND a.startTime >= :startDay AND a.startTime <= :endDay " +
            "ORDER BY a.startTime ASC")
    List<Appointment> findByHairdresserAndEmployeeId(long idHairdresser, long idEmployee,
                                                     LocalDateTime startDay, LocalDateTime endDay);
    @Query("SELECT a FROM Appointment a WHERE a.user.id = :idUser " +
            "ORDER BY a.startTime ASC")
    List<Appointment> findByUser(long idUser);
    Appointment findById(long id);

    @Query("SELECT COUNT(*) FROM Appointment a WHERE a.employee.id = :idEmployee "
            + "AND a.startTime BETWEEN :startTime AND :endTime")
    int countByEmployeeAndDate(@Param("idEmployee") long idEmployee, @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);
}
