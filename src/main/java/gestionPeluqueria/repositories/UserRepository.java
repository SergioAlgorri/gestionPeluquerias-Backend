package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
    @Query("SELECT u FROM User u WHERE "
        + "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
        + "OR LOWER(u.firstSurname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
        + "OR LOWER(u.secondSurname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByPartialName(@Param("searchTerm") String searchTerm, Pageable pageable);
    User findByEmail(String email);
    Page<User> findByRole(Role role, Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);
}