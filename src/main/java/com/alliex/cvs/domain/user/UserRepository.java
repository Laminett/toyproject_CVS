package com.alliex.cvs.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameLike(String username);

    List<User> findByFullNameLike(String fullName);

    List<User> findByEmailLike(String email);

    List<User> findByDepartmentLike(String department);

}
