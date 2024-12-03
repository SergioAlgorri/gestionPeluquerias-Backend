package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.HairdresserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairdresserCompanyRepository extends JpaRepository<HairdresserCompany, Long> {

    public HairdresserCompany findByName(String name);
}
