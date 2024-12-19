package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.dto.UserEmployeeDTOAssembler;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;
import gestionPeluqueria.services.impl.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Object>> findAll(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String email,
                                              @RequestParam(required = false) Role role,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = userService.findAll(name, email, role, pageable);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Page<Object> usersList = users.map(UserEmployeeDTOAssembler::generateDTO);
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } catch (Exception e ) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findById(@PathVariable("id") long idUser) {
        try {
            User user = userService.findById(idUser);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(UserEmployeeDTOAssembler.generateDTO(user), HttpStatus.OK);
        } catch (Exception e ) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@RequestBody RequestUserDTO user) {
        try {
            User userCreated = userService.createUser(user);
            if (userCreated == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(UserEmployeeDTOAssembler.generateDTO(userCreated));
        } catch (Exception e ) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable("id") long idUser, @RequestBody User user) {
        try {
            User userUpdated = userService.updateUser(idUser, user);

            if (userUpdated == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(userUpdated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long idUser) {
        try {
            if (userService.findById(idUser) == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            userService.deleteUser(idUser);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
