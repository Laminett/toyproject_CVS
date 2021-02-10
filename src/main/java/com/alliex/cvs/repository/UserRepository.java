package com.alliex.cvs.repository;

import com.alliex.cvs.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameLike(String username);

    List<User> findByFullNameLike(String fullName);

    List<User> findByEmailLike(String email);

    List<User> findByDepartmentLike(String department);

    @Query("select u from User u left join fetch u.point")
    List<User> findAllWithFetchJoin(Pageable pageable);

}
