package com.lms.repository;

import com.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.managerId = :managerId")
    List<User> findEmployeesByManagerId(Long managerId);

    @Query("SELECT u FROM User u WHERE u.role = 'EMPLOYEE'")
    List<User> findAllEmployees();
}
