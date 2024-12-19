package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.entities.Hairdresser;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final HairdresserRepository hairdresserRepository;

    public UserServiceImpl(UserRepository userRepository, HairdresserRepository hairdresserRepository) {
        this.userRepository = userRepository;
        this.hairdresserRepository = hairdresserRepository;
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
    // Método de creación de usuarios (CLIENT O EMPLOYEE) solo disponible para el usuario con rol ADMIN
    public User createUser(RequestUserDTO user) {
        User userCreated;
        if (user.getRole().equals(Role.CLIENT)) {
            // Crear usuario cliente
            userCreated = new Client(user.getName(), user.getFirstSurname(), user.getSecondSurname(), user.getEmail(),
                    user.getPassword(), user.getBirthDate(), user.getTelephone());
        } else if (user.getRole().equals(Role.EMPLOYEE)) {
            // Crear usuario empleado
            userCreated = new Employee(user.getName(), user.getFirstSurname(), user.getSecondSurname(), user.getEmail(),
                    user.getPassword(), user.getBirthDate(), user.getTelephone());
            Hairdresser hairdresser = hairdresserRepository.findById(user.getIdHairdresser());
            ((Employee) userCreated).setHairdresser(hairdresser);
            hairdresser.addEmployee((Employee) userCreated);
            hairdresserRepository.save(hairdresser);
        } else {
            return null;
        }

        for (User u: userRepository.findAll()) {
            if (u.equals(userCreated)) {
                return null;
            }
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
}
