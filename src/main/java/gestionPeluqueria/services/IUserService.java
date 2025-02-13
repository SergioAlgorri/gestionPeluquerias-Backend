package gestionPeluqueria.services;

import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    Page<User> findAll(String name, String email, Role role, Pageable pageable);
    User findById(long id);
    User findByEmail(String email);
    User createUser(RequestUserDTO user);
    User updateUser(long id, User user);
    void deleteUser(long id);
}