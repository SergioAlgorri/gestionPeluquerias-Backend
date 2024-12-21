package gestionPeluqueria.dto;

public class RequestGuestLoginDTO {

    private String name;
    private String firstSurname;
    private String secondSurname;

    public RequestGuestLoginDTO(String name, String firstSurname, String secondSurname) {
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
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
}