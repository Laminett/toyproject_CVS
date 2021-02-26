package com.alliex.cvs.service;

import com.alliex.cvs.domain.type.UserStatus;
import com.alliex.cvs.entity.User;
import com.alliex.cvs.exception.UserAlreadyExistsException;
import com.alliex.cvs.exception.UserNotFoundException;
import com.alliex.cvs.repository.UserRepository;
import com.alliex.cvs.repository.UserRepositorySupport;
import com.alliex.cvs.web.dto.UserRequest;
import com.alliex.cvs.web.dto.UserResponse;
import com.alliex.cvs.web.dto.UserSaveRequest;
import com.alliex.cvs.web.dto.UserUpdateRequest;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositorySupport userRepositorySupport;

    @Autowired
    private PointService pointService;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));

        // INACTIVE User.
        if (user.getStatus() != UserStatus.ACTIVE) {
            log.info("User " + username + " status is not ACTIVE.");

            throw new UsernameNotFoundException(user.getUsername());
        }

        List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList(user.getRole().getKey());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), USER_ROLES);
    }

    public UserResponse getUserByUsername(String username) {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));

        return new UserResponse(user);
    }

    public UserResponse save(UserSaveRequest userSaveRequest) {
        // Check existence of a user.
        userRepository.findByUsername(userSaveRequest.getUsername()).ifPresent(user -> {
            throw new UserAlreadyExistsException(user.getUsername());
        });

        User user = userSaveRequest.toEntity();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        // Create a user.
        Long id = userRepository.save(user).getId();

        // Create a point.
        pointService.save(id, 0L);

        return new UserResponse(id, user.getUsername());
    }

    @Transactional
    public Long update(Long id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User " + id + " does not exist."));

        if (userUpdateRequest.getDepartment() != null) {
            user.setDepartment(userUpdateRequest.getDepartment());
        }

        if (userUpdateRequest.getFullName() != null) {
            user.setFullName(userUpdateRequest.getFullName());
        }

        if (userUpdateRequest.getEmail() != null) {
            user.setEmail(userUpdateRequest.getEmail());
        }

        if (userUpdateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        }

        if (userUpdateRequest.getStatus() != null) {
            user.setStatus(userUpdateRequest.getStatus());
        }

        if (StringUtils.isNotBlank(userUpdateRequest.getPassword())) {
            user.setPassword(encoder.encode(userUpdateRequest.getPassword()));
        }

        return id;
    }

    // ToDo flag 처리?
    public void delete(Long id) {

    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User id " + id + " does not exist."));

        return new UserResponse(user);
    }

    public Page<UserResponse> getUsers(Pageable pageable, UserRequest userRequest) {
        Page<User> users = userRepositorySupport.findAll(pageable, userRequest);

        return users.map(UserResponse::new);
    }

    public boolean isValidPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }

        if (password.length() < 5) {
            return false;
        }

        if (password.length() > 20) {
            return false;
        }

        return true;
    }

}
