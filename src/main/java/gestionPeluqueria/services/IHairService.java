package gestionPeluqueria.services;

import gestionPeluqueria.entities.composite.ServiceComponent;
import java.util.List;

public interface IHairService {

    public List<ServiceComponent> findAll();
    public ServiceComponent findById(long idService);
    public ServiceComponent createService(ServiceComponent service);
    public ServiceComponent updateService(long idService, ServiceComponent service);
    public void deleteService(long idService);
}
