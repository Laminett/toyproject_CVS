package com.alliex.cvs.service;

import com.alliex.cvs.domain.user.LoginUser;
import com.alliex.cvs.domain.user.User;
import com.alliex.cvs.domain.user.UserRepository;
import com.alliex.cvs.util.AuthoritiesUtils;
import com.alliex.cvs.web.dto.UserRequest;
import com.alliex.cvs.web.dto.UserResponse;
import com.alliex.cvs.web.dto.UserSaveRequest;
import com.alliex.cvs.web.dto.UserUpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));

        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(username);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), AuthoritiesUtils.createAuthorities(loginUser));
    }

    public List<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse getUserByUsername(String username) {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));

        return new UserResponse(user);
    }

    public Long save(UserSaveRequest userSaveRequest) {
        // Password encode.
        User user = userSaveRequest.toEntity();
        user.setPassword(encoder.encode(user.getPassword()));

        // Create user.
        return userRepository.save(user).getId();
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
                .orElseThrow(() -> new IllegalArgumentException("User " + id + " does not exist."));

        return new UserResponse(user);
    }

    public List<UserResponse> getUsers(Pageable pageable, UserRequest userRequest) {
        if (StringUtils.isNotBlank(userRequest.getUsername())) {
            return userRepository.findByUsernameLike(com.alliex.cvs.util.StringUtils.makeLike(userRequest.getUsername())).stream()
                    .map(UserResponse::new)
                    .collect(Collectors.toList());
        } else if (StringUtils.isNotBlank(userRequest.getFullName())) {
            return userRepository.findByFullNameLike(com.alliex.cvs.util.StringUtils.makeLike(userRequest.getFullName())).stream()
                    .map(UserResponse::new)
                    .collect(Collectors.toList());
        } else if (StringUtils.isNotBlank(userRequest.getEmail())) {
            return userRepository.findByEmailLike(com.alliex.cvs.util.StringUtils.makeLike(userRequest.getEmail())).stream()
                    .map(UserResponse::new)
                    .collect(Collectors.toList());
        } else if (StringUtils.isNotBlank(userRequest.getDepartment())) {
            return userRepository.findByDepartmentLike(com.alliex.cvs.util.StringUtils.makeLike(userRequest.getDepartment())).stream()
                    .map(UserResponse::new)
                    .collect(Collectors.toList());
        } else {
            return getUsers(pageable);
        }
    }

}
