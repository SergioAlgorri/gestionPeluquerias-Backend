package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;
import gestionPeluqueria.services.impl.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static gestionPeluqueria.controllers.TestUtils.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl mockUserService;

    private User client1;
    private User client2;
    private User employee1;

    private Page<User> userList;
    private Page<User> emptyList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client1 = new Client("Sergio", "Algorri", "Ruiz", "sergio@gmail.com",
                "sergio123", LocalDate.of(2002,7,14), "667123821");
        client1.setId(1);
        client1.setRole(Role.CLIENT);
        client2 = new Client("Lucía", "Ruiz", "Ruiz", "luciarr@gmail.com",
                "lucia1990", LocalDate.of(1990,10,29), "612521515");
        client2.setId(2);
        client2.setRole(Role.CLIENT);
        employee1 = new Employee("Pedro", "López", "Castillo", "pedrlo@gmail.com",
                "pedrolo123", LocalDate.of(1980,2,1), "699091821");
        employee1.setId(3);
        Hairdresser hairdresser = new Hairdresser();
        hairdresser.setId(1);
        ((Employee) employee1).setHairdresser(hairdresser);

        userList = new PageImpl<>(List.of(client1, client2));
        emptyList = Page.empty();
    }

    @Test
    void findAllTest() throws Exception {
        // No hay usuarios: RETURN 204 NO CONTENT
        when(mockUserService.findAll(null, null, null,
                PageRequest.of(0,10))).thenReturn(emptyList);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Hay usuarios: RETURN 200 OK
        when(mockUserService.findAll(null, null, null,
                PageRequest.of(0,10))).thenReturn(userList);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)));

        // Filtrar usuarios por nombre: RETURN 200 OK
        Page<User> listByName = new PageImpl<>(List.of(client1));
        when(mockUserService.findAll("Sergio", null, null,
                PageRequest.of(0,10))).thenReturn(listByName);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
                        .param("name", "Sergio")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].fullName").value("Sergio Algorri Ruiz"));

        // Filtrar usuarios por email: RETURN 200 OK
        Page<User> listByEmail = new PageImpl<>(List.of(client2));
        when(mockUserService.findAll(null, "luciarr@gmail.com", null,
                PageRequest.of(0,10))).thenReturn(listByEmail);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
                        .param("email", "luciarr@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].email").value("luciarr@gmail.com"));

        // Filtrar usuarios por rol: RETURN 200 OK
        Page<User> listByRole = new PageImpl<>(List.of(client1, client2));
        when(mockUserService.findAll(null, null, Role.CLIENT,
                PageRequest.of(0,10))).thenReturn(listByRole);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
                        .param("role", "CLIENT")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[0].role").value("CLIENT"));
    }

    @Test
    void findByIdTest() throws Exception {
        // Caso No Válido: No existe el usuario (NOT FOUND)
        when(mockUserService.findById(6)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/6")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        // Caso Válido: Existe la Peluquería (OK)
        User userFound = new Client("Sergio", "Algorri", "Ruiz", "sergio@gmail.com",
                "sergio123", LocalDate.of(2002,7,14), "667123821");
        when(mockUserService.findById(userFound.getId())).thenReturn(userFound);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/" +userFound.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userFound.getEmail()));
    }

    @Test
    void createUserTest() throws Exception {
        RequestUserDTO request = new RequestUserDTO();
        request.setName(client2.getName());
        request.setFirstSurname(client2.getFirstSurname());
        request.setSecondSurname(client2.getSecondSurname());
        request.setEmail(client2.getEmail());
        request.setPassword(client2.getPassword());
        request.setTelephone(client2.getTelephone());
        request.setIdHairdresser(1);
        request.setRole(Role.CLIENT);

        // Caso Válido: Se crea por primera vez un usuario (CREATED)
        when(mockUserService.createUser(request)).thenReturn(client2);
        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated());

        // Caso No Válido: Se crea un servicio existente (CONFLICT)
        when(mockUserService.createUser(request)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateUserTest() throws Exception {
        User updatedUser = new User();
        updatedUser.setTelephone("1234");
        client1.setTelephone(updatedUser.getTelephone());

        // Caso No Válido: Usuario No Existe (BAD REQUEST)
        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/6")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(updatedUser)))
                .andExpect(status().isBadRequest());

        // Caso Válido: Usuario Existe (OK)
        when(mockUserService.updateUser(client1.getId(), updatedUser)).thenReturn(client1);
        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/" +client1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telephone").value("1234"));
    }

    @Test
    void deleteUserTest() throws Exception {
        // Caso No Válido: Usuario No Existe (BAD REQUEST)
        when(mockUserService.findById(6)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/6")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        // Caso Válido: Usuario Existe (NO CONTENT)
        when(mockUserService.findById(employee1.getId())).thenReturn(employee1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/" +employee1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}