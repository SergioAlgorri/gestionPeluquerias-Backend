package gestionPeluqueria.entities.Inheritance;

import gestionPeluqueria.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User emptyUser;
    private User paramUser;

    private String name;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String telephone;
    private Role role;

    @BeforeEach
    void setUp() {
        // Constructor vacío
        emptyUser = new User();

        // Constructor parametrizado
        name = "Sergio";
        firstSurname = "Algorri";
        secondSurname = "Ruiz";
        email = "sergio@gmail.com";
        password = "admin";
        birthDate = LocalDate.of(2002, 7, 14);
        telephone = "610203040";
        role = Role.ADMIN;
        paramUser = new User(name, firstSurname, secondSurname, email, password, birthDate, telephone);
        paramUser.setRole(role);
    }

    @Test
    void testDefaultConstructor() {
        assertNull(emptyUser.getName(), "The name should be null by default");
        assertNull(emptyUser.getFirstSurname(), "The first surname should be null by default");
        assertNull(emptyUser.getSecondSurname(), "The second surname should be null by default");
        assertNull(emptyUser.getEmail(), "The email should be null by default");
        assertNull(emptyUser.getPassword(), "The password should be null by default");
        assertNull(emptyUser.getBirthDate(), "The birth date should be null by default");
        assertNull(emptyUser.getTelephone(), "The telephone should be null by default");
        assertNull(emptyUser.getRole(), "The role should be null by default");
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(name, paramUser.getName(), "The name should be equal to the expected value");
        assertEquals(firstSurname, paramUser.getFirstSurname(),
                "The first surname should be equal to the expected value");
        assertEquals(secondSurname, paramUser.getSecondSurname(),
                "The second surname should be equal to the expected value");
        assertEquals(email, paramUser.getEmail(), "The email should be equal to the expected value");
        assertEquals(password, paramUser.getPassword(), "The password should be equal to the expected value");
        assertEquals(birthDate, paramUser.getBirthDate(),
                "The birth date should be equal to the expected value");
        assertEquals(telephone, paramUser.getTelephone(),
                "The telephone should be equal to the expected value");
        assertEquals(role, paramUser.getRole(), "The role should be equal to the expected value");
    }

    @Test
    void testSetAndGetName() {
        emptyUser.setName(name);
        assertEquals(name, emptyUser.getName(), "The name should be equal to the expected value");
    }

    @Test
    void testSetAndGetFirstSurname() {
        emptyUser.setFirstSurname(firstSurname);
        assertEquals(firstSurname, emptyUser.getFirstSurname(),
                "The first surname should be equal to the expected value");
    }

    @Test
    void testSetAndGetSecondSurname() {
        emptyUser.setSecondSurname(secondSurname);
        assertEquals(secondSurname, emptyUser.getSecondSurname(),
                "The second surname should be equal to the expected value");
    }

    @Test
    void testSetAndGetEmail() {
        emptyUser.setEmail(email);
        assertEquals(email, emptyUser.getEmail(), "The email should be equal to the expected value");
    }

    @Test
    void testSetAndGetPassword() {
        emptyUser.setPassword(password);
        assertEquals(password, emptyUser.getPassword(), "The password should be equal to the expected value");
    }

    @Test
    void testSetAndGetBirthDate() {
        emptyUser.setBirthDate(birthDate);
        assertEquals(birthDate, emptyUser.getBirthDate(),
                "The birth date should be equal to the expected value");
    }

    @Test
    void testSetAndGetTelephone() {
        emptyUser.setTelephone(telephone);
        assertEquals(telephone, emptyUser.getTelephone(),
                "The telephone should be equal to the expected value");
    }

    @Test
    void testSetAndGetRole() {
        emptyUser.setRole(role);
        assertEquals(role, emptyUser.getRole(), "The role should be equal to the expected value");
    }

    @Test
    void testClient() {
        Client client = new Client();
        assertEquals(Role.CLIENT, client.getRole(), "The role should be equal to the expected value");
    }

    @Test
    void testEmployee() {
        Employee employee = new Employee();
        assertEquals(Role.EMPLOYEE, employee.getRole(), "The role should be equal to the expected value");
    }

    @Test
    void testGuest() {
        Guest guest = new Guest();
        assertEquals(Role.GUEST, guest.getRole(), "The role should be equal to the expected value");
    }

    @Test
    void testEqualsAndHashCode() {
        User user2 = new User("Sergio", "Algorri", "Ruiz", "sergio@gmail.com",
                "admin", LocalDate.of(2002,7,14), "610203040");
        user2.setRole(Role.ADMIN);
        User user3 = new User("María", "Pérez", "Martínez", "maria@gmail.com",
                "123", LocalDate.of(1990,10,8), "699090807");
        user3.setRole(Role.GUEST);

        assertEquals(paramUser, user2, "Users with the same values should be equal");
        assertNotEquals(paramUser, user3, "Users with different values should not be equal");
        assertEquals(paramUser.hashCode(), user2.hashCode(), "Equal users should have the same hash code");
    }
}
