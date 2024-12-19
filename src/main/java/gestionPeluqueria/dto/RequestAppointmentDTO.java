package gestionPeluqueria.dto;

import gestionPeluqueria.entities.composite.ServiceComponent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class RequestAppointmentDTO {

    private LocalDateTime startTime;
    private long idUser;
    private Long idEmployee;
    private long idService;
    private Long idReward;

    public RequestAppointmentDTO() {
    }

    public RequestAppointmentDTO(LocalDateTime startTime, long idUser, Long idEmployee,
                                 long idService, Long idReward) {
        this.startTime = startTime;
        this.idUser = idUser;
        this.idEmployee = idEmployee;
        this.idService = idService;
        this.idReward = idReward;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public long getIdService() {
        return idService;
    }

    public void setIdService(long idService) {
        this.idService = idService;
    }

    public Long getIdReward() {
        return idReward;
    }

    public void setIdReward(Long idReward) {
        this.idReward = idReward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestAppointmentDTO that = (RequestAppointmentDTO) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(idUser, that.idUser)
                && Objects.equals(idEmployee, that.idEmployee) && Objects.equals(idService, that.idService)
                && Objects.equals(idReward, that.idReward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, idUser, idEmployee, idService, idReward);
    }
}
