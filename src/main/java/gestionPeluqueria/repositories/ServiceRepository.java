package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.composite.ServiceComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceComponent, Long> {

    public ServiceComponent findById(long id);
}
