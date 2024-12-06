package gestionPeluqueria.services.impl;

import gestionPeluqueria.entities.composite.ServiceComponent;
import gestionPeluqueria.repositories.ServiceRepository;
import gestionPeluqueria.services.IHairService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HairServiceImpl implements IHairService {

    private final ServiceRepository serviceRepository;

    public HairServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<ServiceComponent> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public ServiceComponent findById(long idService) {
        ServiceComponent service = serviceRepository.findById(idService);

        if (service == null) {
            return null;
        }

        return service;
    }

    @Override
    public ServiceComponent createService(ServiceComponent service) {
        for (ServiceComponent sc: this.findAll()) {
            if (sc.equals(service)) {
                return null;
            }
        }

        return serviceRepository.save(service);
    }

    @Override
    public ServiceComponent updateService(long idService, ServiceComponent service) {
        ServiceComponent serviceFound = this.findById(idService);

        if (serviceFound == null) {
            return null;
        }

        if (service.getName() != null) {
            serviceFound.setName(service.getName());
        }

        if (service.getDescription() != null) {
            serviceFound.setDescription(service.getDescription());
        }

        if (service.getPrice() != null) {
            serviceFound.setPrice(service.getPrice());
        }

        if (service.getDuration() != null) {
            serviceFound.setDuration(service.getDuration());
        }

        return serviceRepository.save(serviceFound);
    }

    @Override
    public void deleteService(long idService) {
        ServiceComponent service = this.findById(idService);

        if (service == null) {
            return;
        }

        serviceRepository.delete(service);
    }
}
