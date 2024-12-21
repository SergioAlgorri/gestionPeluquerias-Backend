package gestionPeluqueria.dto;

public class ChangePasswordDTO {

    private String newPassword;
    private String repeatNewPassword;

    public ChangePasswordDTO(String newPassword, String repeatNewPassword) {
        this.newPassword = newPassword;
        this.repeatNewPassword = repeatNewPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }
}
