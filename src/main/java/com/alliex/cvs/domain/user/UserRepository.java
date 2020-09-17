package com.alliex.cvs.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByFullName(String fullName);

    List<User> findByEmailLike(String email);

    List<User> findByDepartment(String department);
}
