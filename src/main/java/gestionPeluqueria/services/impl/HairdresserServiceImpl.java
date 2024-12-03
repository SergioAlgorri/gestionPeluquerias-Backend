package gestionPeluqueria.services.impl;

import gestionPeluqueria.entities.Hairdresser;
import gestionPeluqueria.entities.HairdresserCompany;
import gestionPeluqueria.repositories.HairdresserCompanyRepository;
import gestionPeluqueria.repositories.HairdresserRepository;
import gestionPeluqueria.services.IHairdresserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HairdresserServiceImpl implements IHairdresserService {

    private final HairdresserRepository hairdresserRepository;
    private final HairdresserCompanyRepository hairdresserCompanyRepository;
    private String name = "Peluquería UNICAN";

    @Autowired
    public HairdresserServiceImpl(HairdresserRepository hairdresserRepository,
                                  HairdresserCompanyRepository hairdresserCompanyRepository) {
        this.hairdresserRepository = hairdresserRepository;
        this.hairdresserCompanyRepository = hairdresserCompanyRepository;
    }


    @Override
    public List<Hairdresser> findAll() {
        return hairdresserRepository.findAll();
    }

    @Override
    public Hairdresser findById(long id) {
        Hairdresser hairdresser = hairdresserRepository.findById(id);

        if (hairdresser == null) {
            return null;
        }

        return hairdresser;
    }

    @Override
    public Hairdresser createHairdresser(Hairdresser hairdresser) {
        for (Hairdresser h: this.findAll()) {
            if (h.equals(hairdresser)) {
                return null;
            }
        }

        HairdresserCompany hairdresserCompany = hairdresserCompanyRepository.findByName(name);
        hairdresser.setCompany(hairdresserCompany);
        return hairdresserRepository.save(hairdresser);
    }

    @Override
    public Hairdresser updateHairdresser(long idHairdresser, Hairdresser hairdresser) {
        Hairdresser hairdresserFound = hairdresserRepository.findById(idHairdresser);

        if (hairdresserFound ==  null) {
            return null;
        }

        // Update Hairdresser
        if (hairdresser.getOpeningTime() != null) {
            hairdresserFound.setOpeningTime(hairdresser.getOpeningTime());
        }

        if (hairdresser.getClosingTime() != null) {
            hairdresserFound.setClosingTime(hairdresser.getClosingTime());
        }

        if (hairdresser.getAddress() != null) {
            hairdresserFound.setAddress(hairdresser.getAddress());
        }

        if (hairdresser.getTelephone() != null) {
            hairdresserFound.setTelephone(hairdresser.getTelephone());
        }

        return hairdresserRepository.save(hairdresserFound);
    }

    @Override
    public void deleteHairdresser(long idHairdresser) {
        Hairdresser hairdresser = this.findById(idHairdresser);
        hairdresserRepository.delete(hairdresser);
    }
}