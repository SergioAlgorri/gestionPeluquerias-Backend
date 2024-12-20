package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    Reward findById(long id);
    List<Reward> findByExpirationDateBefore(LocalDate currentDate);
}
