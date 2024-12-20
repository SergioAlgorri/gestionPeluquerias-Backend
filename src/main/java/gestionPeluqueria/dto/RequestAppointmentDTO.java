package gestionPeluqueria.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class RequestAppointmentDTO {

    private LocalDateTime startTime;
    private Long idUser;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private Long idEmployee;
    private long idService;
    private Long idReward;

    public RequestAppointmentDTO() {
    }

    public RequestAppointmentDTO(LocalDateTime startTime, Long idUser, String name, String firstSurname,
                                 String secondSurname, Long idEmployee, long idService, Long idReward) {
        this.startTime = startTime;
        this.idUser = idUser;
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
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
        return idService == that.idService && Objects.equals(startTime, that.startTime)
                && Objects.equals(idUser, that.idUser) && Objects.equals(name, that.name)
                && Objects.equals(firstSurname, that.firstSurname) && Objects.equals(secondSurname, that.secondSurname)
                && Objects.equals(idEmployee, that.idEmployee) && Objects.equals(idReward, that.idReward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, idUser, name, firstSurname, secondSurname, idEmployee, idService, idReward);
    }
}
