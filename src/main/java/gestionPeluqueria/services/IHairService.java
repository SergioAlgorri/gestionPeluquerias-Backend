package gestionPeluqueria.services;

import gestionPeluqueria.dto.RequestServiceDTO;
import gestionPeluqueria.entities.composite.ServiceComponent;
import java.util.List;

public interface IHairService {

    public List<ServiceComponent> findAll();
    public ServiceComponent findById(long idService);
    public ServiceComponent createService(RequestServiceDTO service);
    public ServiceComponent updateService(long idService, ServiceComponent service);
    public void deleteService(long idService);
}
