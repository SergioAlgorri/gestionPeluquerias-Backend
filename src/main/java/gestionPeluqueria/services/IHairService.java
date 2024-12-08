package gestionPeluqueria.services;

import gestionPeluqueria.dto.RequestServiceDTO;
import gestionPeluqueria.entities.composite.ServiceComponent;
import java.util.List;

public interface IHairService {

    List<ServiceComponent> findAll();
    ServiceComponent findById(long idService);
    ServiceComponent createService(RequestServiceDTO service);
    ServiceComponent updateService(long idService, ServiceComponent service);
    void deleteService(long idService);
}
