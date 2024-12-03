package gestionPeluqueria.dto;

public class EmployeeDTO {

    private String name;

    public EmployeeDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}