package gestionPeluqueria.repositories;

import gestionPeluqueria.entities.Inheritance.User;
import gestionPeluqueria.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE "
            + "(:nameSearch IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :nameSearch, '%')) "
            + "OR LOWER(u.firstSurname) LIKE LOWER(CONCAT('%', :nameSearch, '%')) "
            + "OR LOWER(u.secondSurname) LIKE LOWER(CONCAT('%', :nameSearch, '%'))) "
            + "AND (:emailSearch IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :emailSearch, '%'))) "
            + "AND (:role IS NULL OR u.role = :role)")
    Page<User> findByFilters(@Param("nameSearch") String searchTerm,
                             @Param("emailSearch") String emailSearch,
                             @Param("role") Role role,
                             Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePassword(@Param("email") String email,
                        @Param("password") String password);
}

/*
    @Query("SELECT u FROM User u WHERE "
        + "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
        + "OR LOWER(u.firstSurname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
        + "OR LOWER(u.secondSurname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByPartialName(@Param("searchTerm") String searchTerm, Pageable pageable);
    @Query("SELECT u FROM User u WHERE "
            + "LOWER(u.email) LIKE LOWER(CONCAT('%', :emailSearch, '%'))")
    Page<User> findByPartialEmail(@Param("emailSearch") String emailSearch, Pageable pageable);
    Page<User> findByRole(Role role, Pageable pageable);
 */