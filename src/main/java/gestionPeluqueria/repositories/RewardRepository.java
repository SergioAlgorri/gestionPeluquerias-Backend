package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    public Reward findById(long id);
}
