package gestionPeluqueria.services.impl;

import gestionPeluqueria.dto.RequestServiceDTO;
import gestionPeluqueria.entities.composite.CompositeService;
import gestionPeluqueria.entities.composite.ServiceComponent;
import gestionPeluqueria.entities.composite.SimpleService;
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
    public ServiceComponent createService(RequestServiceDTO service) {
        // Crear el servicio
        ServiceComponent serviceCreated;

        // Servicio Simple
        if (service.getServices() == null) {
            serviceCreated = new SimpleService(service.getName(), service.getDescription(),
                    service.getPrice(), service.getDuration());
        } else {
            // Servicio Compuesto
            serviceCreated = new CompositeService(service.getName(), service.getDescription());
            for (Long serviceId: service.getServices()) {
                ServiceComponent serviceComponent = this.findById(serviceId);
                ((CompositeService) serviceCreated).addService(serviceComponent);
            }
        }

        // Comprobar si está duplicado
        for (ServiceComponent sc: this.findAll()) {
            if (sc.equals(serviceCreated)) {
                return null;
            }
        }

        return serviceRepository.save(serviceCreated);
    }

    @Override
    public ServiceComponent updateService(long idService, ServiceComponent service) {
        ServiceComponent serviceFound = this.findById(idService);

        if (serviceFound == null) {
            return null;
        }

        // Update Service
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