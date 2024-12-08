package gestionPeluqueria.services;

import gestionPeluqueria.entities.Hairdresser;

import java.util.List;

public interface IHairdresserService {

    List<Hairdresser> findAll();
    Hairdresser findById(long id);
    Hairdresser createHairdresser(Hairdresser hairdresser);
    Hairdresser updateHairdresser(long idHairdresser, Hairdresser hairdresser);
    void deleteHairdresser(long idHairdresser);
}
