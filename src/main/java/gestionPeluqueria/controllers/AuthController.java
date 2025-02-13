package gestionPeluqueria.controllers;

import gestionPeluqueria.dto.RequestGuestLoginDTO;
import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.dto.RequestUserLoginDTO;
import gestionPeluqueria.services.impl.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String ERROR_MESSAGE_REGISTER = "Usuario Ya Registrado";

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody RequestUserDTO user) {
        String message = authService.register(user);
        if (message.equals(ERROR_MESSAGE_REGISTER)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody RequestUserLoginDTO login) {
        String token = authService.login(login);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/token_guest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> guestLogin(@RequestBody RequestGuestLoginDTO request) {
        String token = authService.loginAsGuest(request);
        return ResponseEntity.ok(token);
    }
}