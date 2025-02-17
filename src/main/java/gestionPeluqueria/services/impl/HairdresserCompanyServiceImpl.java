package gestionPeluqueria.services.impl;

import gestionPeluqueria.entities.HairdresserCompany;
import gestionPeluqueria.repositories.HairdresserCompanyRepository;

public class HairdresserCompanyServiceImpl {

    private final HairdresserCompanyRepository hairdresserCompanyRepository;

    public HairdresserCompanyServiceImpl(HairdresserCompanyRepository hairdresserCompanyRepository) {
        this.hairdresserCompanyRepository = hairdresserCompanyRepository;
    }

    public HairdresserCompany findByName(String name) {
        HairdresserCompany hairdresserCompany =  hairdresserCompanyRepository.findByName(name);

        if (hairdresserCompany == null) {
            return null;
        }

        return hairdresserCompany;
    }
}