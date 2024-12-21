package gestionPeluqueria.services;

import gestionPeluqueria.dto.MailBodyDTO;

public interface IEmailService {

    void sendSimpleMessage(MailBodyDTO mailBody);

}
