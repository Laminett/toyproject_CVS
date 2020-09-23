package com.alliex.cvs.service;

import com.alliex.cvs.domain.type.UserSearchType;
import com.alliex.cvs.domain.user.User;
import com.alliex.cvs.domain.user.UserRepository;
import com.alliex.cvs.exception.UserAlreadyExistsException;
import com.alliex.cvs.web.dto.UserRequest;
import com.alliex.cvs.web.dto.UserResponse;
import com.alliex.cvs.web.dto.UserSaveRequest;
import com.alliex.cvs.web.dto.UserUpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
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

        List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList(user.getRole().getKey());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), USER_ROLES);
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
        // Check existence of an user.
        userRepository.findByUsername(userSaveRequest.getUsername()).ifPresent(user -> {
            throw new UserAlreadyExistsException(userSaveRequest.getUsername());
        });

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

    public Page<UserResponse> getUsers(Pageable pageable, UserRequest userRequest) {
        Page<UserResponse> entities = userRepository.findAll(searchWith(getPredicateData(userRequest)), pageable)
                .map(UserResponse::new);

        return entities;
    }

    private Specification<User> searchWith(Map<UserSearchType, String> predicateData) {
        return (Specification<User>) ((root, query, builder) -> {
            List<Predicate> predicate = new ArrayList<>();
            for (Map.Entry<UserSearchType, String> entry : predicateData.entrySet()) {
                predicate.add(builder.like(
                        root.get(entry.getKey().getField()), "%" + entry.getValue() + "%"
                ));
            }

            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private Map<UserSearchType, String> getPredicateData(UserRequest userRequest) {
        Map<UserSearchType, String> predicateData = new HashMap<>();

        if (StringUtils.isNotBlank(userRequest.getUsername())) {
            predicateData.put(UserSearchType.USERNAME, userRequest.getUsername());
        }

        if (StringUtils.isNotBlank(userRequest.getFullName())) {
            predicateData.put(UserSearchType.FULL_NAME, userRequest.getFullName());
        }

        if (StringUtils.isNotBlank(userRequest.getEmail())) {
            predicateData.put(UserSearchType.EMAIL, userRequest.getEmail());
        }

        if (StringUtils.isNotBlank(userRequest.getDepartment())) {
            predicateData.put(UserSearchType.DEPARTMENT, userRequest.getDepartment());
        }

        return predicateData;
    }

}
