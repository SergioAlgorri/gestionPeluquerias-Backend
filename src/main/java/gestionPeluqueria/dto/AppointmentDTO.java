package gestionPeluqueria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDTO {

    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String comment;
    private long idClient;
    private String userName;
    private String serviceName;
    private String rewardName;
    private String employeeName;
    private String hairdresserAddress;
    private BigDecimal price;

    public AppointmentDTO(LocalDateTime startTime, LocalDateTime endTime, String comment, String username,
                          String serviceName, String employeeName, String hairdresserAddress, BigDecimal price) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
        this.userName = username;
        this.serviceName = serviceName;
        this.employeeName = employeeName;
        this.hairdresserAddress = hairdresserAddress;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getHairdresserAddress() {
        return hairdresserAddress;
    }

    public void setHairdresserAddress(String hairdresserAddress) {
        this.hairdresserAddress = hairdresserAddress;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}