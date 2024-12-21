package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.Inheritance.Admin;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Employee;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;
import gestionPeluqueria.repositories.HairdresserRepository;
import gestionPeluqueria.repositories.UserRepository;
import gestionPeluqueria.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final HairdresserRepository hairdresserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, HairdresserRepository hairdresserRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.hairdresserRepository = hairdresserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<User> findAll(String name, String email, Role role, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            return userRepository.findByName(name, pageable);
        }

        if (email != null && !email.isEmpty()) {
            User userByEmail = userRepository.findByEmail(email);
            return new PageImpl<>(List.of(userByEmail), pageable, 1);
        }

        if (role != null) {
            return userRepository.findByRole(role, pageable);
        }

        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(long id) {
        User user = userRepository.findById(id);

        if (user == null) {
            return null;
        }

        return user;
    }

    @Override
    // Método de creación de usuarios (CLIENT, EMPLOYEE o ADMIN) solo disponible para el usuario con rol ADMIN
    public User createUser(RequestUserDTO user) {
        // Comprobamos que no existe otro usuario con el mismo email
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return null;
        }

        // Encriptamos la contraseña
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Creamos el usuario según el rol
        User userCreated = createUserByRole(user, encodedPassword);

        // Rol No Válido
        if (userCreated == null) {
            return null;
        }

        return userRepository.save(userCreated);
    }

    @Override
    public User updateUser(long id, User user) {
        User userFound = this.findById(id);

        if (userFound == null) {
            return null;
        }

        // Update User
        if (user.getName() != null) {
            userFound.setName(user.getName());
        }

        if (user.getFirstSurname() != null) {
            userFound.setFirstSurname(user.getFirstSurname());
        }

        if (user.getSecondSurname() != null) {
            userFound.setSecondSurname(user.getSecondSurname());
        }

        if (user.getTelephone() != null) {
            userFound.setTelephone(user.getTelephone());
        }

        return userRepository.save(userFound);
    }

    @Override
    public void deleteUser(long id) {
        User user = this.findById(id);

        if (user == null) {
            return;
        }

        userRepository.delete(user);
    }

    // Métodos Auxiliares

    private User createUserByRole(RequestUserDTO user, String encodedPassword) {
        return switch (user.getRole()) {
            case CLIENT -> new Client(user.getName(), user.getFirstSurname(), user.getSecondSurname(), user.getEmail(),
                    encodedPassword, user.getBirthDate(), user.getTelephone());
            case EMPLOYEE -> createEmployee(user, encodedPassword);
            case ADMIN -> new Admin(user.getName(), user.getFirstSurname(), user.getSecondSurname(), user.getEmail(),
                    encodedPassword, user.getBirthDate(), user.getTelephone());
            default -> null;
        };
    }

    private Employee createEmployee(RequestUserDTO user, String encodedPassword) {
        Hairdresser hairdresser = hairdresserRepository.findById(user.getIdHairdresser());

        Employee employee = new Employee(user.getName(), user.getFirstSurname(), user.getSecondSurname(),
                user.getEmail(), encodedPassword, user.getBirthDate(), user.getTelephone());

        employee.setHairdresser(hairdresser);
        hairdresser.addEmployee(employee);
        hairdresserRepository.save(hairdresser);
        return employee;
    }
}