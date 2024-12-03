package gestionPeluqueria.services;

import gestionPeluqueria.entities.Hairdresser;

import java.util.List;

public interface IHairdresserService {

    public List<Hairdresser> findAll();
    public Hairdresser findById(long id);
    public Hairdresser createHairdresser(Hairdresser hairdresser);
    public Hairdresser updateHairdresser(long idHairdresser, Hairdresser hairdresser);
    public void deleteHairdresser(long idHairdresser);
}
