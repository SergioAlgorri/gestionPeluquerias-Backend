package gestionPeluqueria.services;

import gestionPeluqueria.dto.RequestGuestLoginDTO;
import gestionPeluqueria.dto.RequestUserDTO;
import gestionPeluqueria.dto.RequestUserLoginDTO;

public interface IAuthService {

    String register(RequestUserDTO user);
    String login(RequestUserLoginDTO loginRequest);
    String loginAsGuest(RequestGuestLoginDTO loginRequest);
}