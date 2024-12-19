package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
    @Query("SELECT u FROM User u WHERE "
        + "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
        + "OR LOWER(u.firstSurname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
        + "OR LOWER(u.secondSurname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByName(@Param("searchTerm") String searchTerm, Pageable pageable);
    User findByEmail(String email);
    Page<User> findByRole(Role role, Pageable pageable);
}
