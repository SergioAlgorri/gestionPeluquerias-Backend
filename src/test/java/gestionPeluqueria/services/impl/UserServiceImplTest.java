package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;
import gestionPeluqueria.repositories.HairdresserRepository;
import gestionPeluqueria.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public class UserServiceImplTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private HairdresserRepository mockHairdresserRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private User client1;
    private User client2;
    private User client3;
    private User employee1;
    private Page<User> userList;
    private Page<User> emptyList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(mockUserRepository, mockHairdresserRepository);

        client1 = new Client("Sergio", "Algorri", "Ruiz", "sergio@gmail.com",
                "sergio123", LocalDate.of(2002,7,14), "667123821");
        client1.setId(1);
        client2 = new Client("Lucía", "Ruiz", "Ruiz", "luciarr@gmail.com",
                "lucia1990", LocalDate.of(1990,10,29), "612521515");
        client2.setId(2);
        client3 = new Client("Sergio", "Martinez", "Perez", "sergiopm@gmail.com",
                "martperser1", LocalDate.of(2010,8,30), "611521252");
        client3.setId(3);
        employee1 = new Employee("Pedro", "López", "Castillo", "pedrlo@gmail.com",
                "pedrolo123", LocalDate.of(1980,2,1), "699091821");
        employee1.setId(4);

        userList = new PageImpl<>(List.of(client1, client2, client3, employee1));
        emptyList = Page.empty();

        when(mockUserRepository.findById(client1.getId())).thenReturn(client1);
        when(mockUserRepository.findById(client2.getId())).thenReturn(client2);
        when(mockUserRepository.findById(client3.getId())).thenReturn(client3);
        when(mockUserRepository.findById(employee1.getId())).thenReturn(employee1);
        when(mockUserRepository.findById(6)).thenReturn(null);
    }

    @Test
    void findAllTest() {
        // Lista Vacía
        when(mockUserRepository.findAll(any(Pageable.class))).thenReturn(emptyList);
        emptyList = userService.findAll(null, null, null,
                PageRequest.of(0,10));
        verify(mockUserRepository).findAll(any(Pageable.class));
        assertTrue(emptyList.isEmpty(), "The list of users should be empty");

        // Todos los clientes
        when(mockUserRepository.findAll(any(Pageable.class))).thenReturn(userList);
        Page<User> result = userService.findAll(null, null, null,
                PageRequest.of(0,10));
        verify(mockUserRepository, times(2)).findAll(any(Pageable.class));
        assertFalse(result.isEmpty(), "The list of users should NOT be empty");
        assertEquals(userList.getContent().size(), result.getContent().size(),
                "The number of users should be equal to the expected value");
        assertEquals(client1, result.getContent().get(0), "The user should be equal to the expected value");
        assertEquals(client2, result.getContent().get(1), "The user should be equal to the expected value");
        assertEquals(client3, result.getContent().get(2), "The user should be equal to the expected value");
        assertEquals(employee1, result.getContent().get(3), "The user should be equal to the expected value");

        // Filtrar por Nombre (findByName)
        Page<User> listByName = new PageImpl<>(List.of(client1, client3));
        when(mockUserRepository.findByName(eq("Sergio"), any(Pageable.class))).thenReturn(listByName);
        Page<User> resultByName = userService.findAll("Sergio", null, null,
                PageRequest.of(0,10));
        verify(mockUserRepository).findByName(eq("Sergio"), any(Pageable.class));
        assertFalse(resultByName.isEmpty(), "The list of users should NOT be empty");
        assertEquals(listByName.getContent().size(), resultByName.getContent().size(),
                "The number of users should be equal to the expected value");
        assertEquals(client1, resultByName.getContent().get(0),
                "The user should be equal to the expected value");
        assertEquals(client3, resultByName.getContent().get(1),
                "The user should be equal to the expected value");

        // Filtrar por Email (findByEmail)
        when(mockUserRepository.findByEmail(eq("luciarr@gmail.com"))).thenReturn(client2);
        Page<User> resultByEmail = userService.findAll(null, "luciarr@gmail.com", null,
                PageRequest.of(0,10));
        verify(mockUserRepository).findByEmail(eq("luciarr@gmail.com"));
        assertFalse(resultByEmail.isEmpty(), "The list of users should NOT be empty");
        assertEquals(1, resultByEmail.getContent().size(),
                "The number of users should be equal to the expected value");
        assertEquals(client2, resultByEmail.getContent().get(0), "The user should be equal to the expected value");

        // Filtrar por Rol (findByRole)
        Page<User> listByRole = new PageImpl<>(List.of(employee1));
        when(mockUserRepository.findByRole(eq(Role.EMPLOYEE), any(Pageable.class))).thenReturn(listByRole);
        Page<User> resultByRole = userService.findAll(null, null, Role.EMPLOYEE,
                PageRequest.of(0,10));
        verify(mockUserRepository).findByRole(eq(Role.EMPLOYEE),any(Pageable.class));
        assertFalse(resultByRole.isEmpty(), "The list of users should NOT be empty");
        assertEquals(listByRole.getContent().size(), resultByRole.getContent().size(),
                "The number of users should be equal to the expected value");
        assertEquals(employee1, resultByRole.getContent().get(0),
                "The user should be equal to the expected value");
    }

    @Test
    void findByIdTest() {
        // Usuario no existente
        assertNull(userService.findById(6), "The user should be null");

        // Usuario existente
        assertEquals(client1, mockUserRepository.findById(client1.getId()),
                "The user should be equal to the expected value");
        verify(mockUserRepository).findById(1);
        assertEquals(client2, mockUserRepository.findById(client2.getId()),
                "The user should be equal to the expected value");
        verify(mockUserRepository).findById(2);
        assertEquals(client3, mockUserRepository.findById(client3.getId()),
                "The user should be equal to the expected value");
        verify(mockUserRepository).findById(3);
        assertEquals(employee1, mockUserRepository.findById(employee1.getId()),
                "The user should be equal to the expected value");
        verify(mockUserRepository).findById(4);
    }

    @Test
    void createUserTest() {
        // Crear cliente por primera vez
        User client4 = new Client("Fede", "Perez", "Ruiz", "fedepr@gmail.com",
                "fede34", LocalDate.of(2004,8,11), "689102034");
        when(mockUserRepository.save(client4)).thenReturn(client4);
        RequestUserDTO request = new RequestUserDTO();
        request.setName(client4.getName());
        request.setFirstSurname(client4.getFirstSurname());
        request.setSecondSurname(client4.getSecondSurname());
        request.setEmail(client4.getEmail());
        request.setPassword(client4.getPassword());
        request.setTelephone(client4.getTelephone());
        request.setRole(Role.CLIENT);

        User userCreated = userService.createUser(request);
        assertEquals(client4, userCreated, "The user should be equal");
        assertEquals(client4.getEmail(), userCreated.getEmail(),
                "The email should be equal to the expected value");

        // Crear empleado por primera vez
        Employee employee2 = new Employee("Ana", "Castro", "Ruiz", "anac@gmail.com",
                "ana1991", LocalDate.of(1991,10,2), "611223344");
        Hairdresser hairdresser = new Hairdresser();
        hairdresser.setId(1);
        employee2.setHairdresser(hairdresser);

        when(mockHairdresserRepository.findById(employee2.getHairdresser().getId())).thenReturn(hairdresser);
        when(mockUserRepository.save(employee2)).thenReturn(employee2);
        RequestUserDTO request2 = new RequestUserDTO();
        request2.setName(employee2.getName());
        request2.setFirstSurname(employee2.getFirstSurname());
        request2.setSecondSurname(employee2.getSecondSurname());
        request2.setEmail(employee2.getEmail());
        request2.setPassword(employee2.getPassword());
        request2.setTelephone(employee2.getTelephone());
        request2.setIdHairdresser(employee2.getHairdresser().getId());
        request2.setRole(Role.EMPLOYEE);

        User employeeCreated = userService.createUser(request2);
        assertEquals(employee2, employeeCreated, "The user should be equal");
        assertEquals(employee2.getEmail(), employeeCreated.getEmail(),
                "The email should be equal to the expected value");
        assertEquals(hairdresser, ((Employee) employeeCreated).getHairdresser(),
                "The hairdresser should be equal to the expected value");

        // Se vuelve a crear el mismo usuario
        when(mockUserRepository.save(client4)).thenReturn(null);
        assertNull(userService.createUser(request), "The user should be null");
    }

    @Test
    void updateUserTest() {
        User userUpdated = new Client();
        userUpdated.setTelephone("1234");

        // Se quiere actualizar un cliente no exitente
        assertNull(userService.updateUser(6, userUpdated), "The result should be null");

        // Se actualiza un cliente existente
        assertEquals("667123821", client1.getTelephone(),
                "The telephone should be equal to the expected value");
        userService.updateUser(1, userUpdated);
        verify(mockUserRepository).findById(client1.getId());
        verify(mockUserRepository).save(client1);
        assertNotEquals("667123821", client1.getTelephone(),
                "The telephone should not be equal to the expected value");
        assertEquals(userUpdated.getTelephone(), client1.getTelephone(),
                "The telephone should be equal to the expected value");
    }

    @Test
    void deleteUserTest() {
        // Se quiere eliminar un cliente no existente (No Válido)
        userService.deleteUser(6);
        verify(mockUserRepository, never()).delete(any(User.class));

        // Se elimina un servicio existente (Válido)
        userService.deleteUser(client2.getId());
        verify(mockUserRepository).delete(client2);
    }
}
