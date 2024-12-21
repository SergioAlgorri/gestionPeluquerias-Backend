package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.RequestGuestLoginDTO;
import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.dto.RequestUserLoginDTO;
import gestionPeluqueria.entities.Inheritance.Client;
import gestionPeluqueria.entities.Inheritance.Guest;
import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.repositories.UserRepository;
import gestionPeluqueria.security.CustomUserDetails;
import gestionPeluqueria.security.CustomUserDetailsService;
import gestionPeluqueria.security.JwtUtil;
import gestionPeluqueria.services.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil,
                           AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(RequestUserDTO user) {
        User userRegistered = userRepository.findByEmail(user.getEmail());
        if (userRegistered != null) {
            return "Usuario Ya Regsitrado";
        }

        // Crear usuario cliente
        userRegistered = new Client(user.getName(), user.getFirstSurname(), user.getSecondSurname(), user.getEmail(),
                passwordEncoder.encode(user.getPassword()), user.getBirthDate(), user.getTelephone());

        userRepository.save(userRegistered);
        return "Usuario Registrado Correctamente";
    }

    @Override
    public String login(RequestUserLoginDTO loginRequest) {
        // Autenticar Usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Obtener Usuario
        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(loginRequest.getEmail());

        return jwtUtil.generateToken(userDetails.getUser());
    }

    @Override
    public String loginAsGuest(RequestGuestLoginDTO loginRequest) {
        User guestUser = new Guest(loginRequest.getName(), loginRequest.getFirstSurname(),
                loginRequest.getSecondSurname());
        userRepository.save(guestUser);
        return jwtUtil.generateToken(guestUser);
    }
}