package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.Hairdresser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairdresserRepository extends JpaRepository<Hairdresser, Long> {

    public Hairdresser findById(long id);
}
