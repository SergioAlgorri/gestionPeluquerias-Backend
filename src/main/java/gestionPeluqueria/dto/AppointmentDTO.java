package gestionPeluqueria.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String serviceName;
    private String rewardName;
    private String employeeName;
    private BigDecimal price;

    public AppointmentDTO(LocalDateTime startTime, LocalDateTime endTime, String serviceName, String rewardName,
                          String employeeName, BigDecimal price) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceName = serviceName;
        this.rewardName = rewardName;
        this.employeeName = employeeName;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
