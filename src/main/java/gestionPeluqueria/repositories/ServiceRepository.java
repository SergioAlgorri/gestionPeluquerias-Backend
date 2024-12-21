package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.composite.ServiceComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceComponent, Long> {

    ServiceComponent findById(long id);
}