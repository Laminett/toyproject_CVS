package com.alliex.cvs.web;

import com.alliex.cvs.service.UserService;
import com.alliex.cvs.web.dto.UserRequest;
import com.alliex.cvs.web.dto.UserResponse;
import com.alliex.cvs.web.dto.UserSaveRequest;
import com.alliex.cvs.web.dto.UserUpdateRequest;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @ApiOperation(value = "Create User")
    @PostMapping({"api/v1/users", "web-api/v1/users"})
    public Long save(@RequestBody UserSaveRequest userSaveRequest) {
        return userService.save(userSaveRequest);
    }

    @ApiOperation(value = "Update User")
    @PostMapping({"api/v1/users/{id}", "web-api/v1/users/{id}"})
    public Long update(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.update(id, userUpdateRequest);
    }

    @ApiOperation(value = "Get Users")
    @GetMapping("/web-api/v1/users")
    public List<UserResponse> getUsers(Pageable pageable, UserRequest userRequest) {
        return userService.getUsers(pageable, userRequest);
    }

    @ApiOperation(value = "Get User")
    @GetMapping({"api/v1/users/{id}", "web-api/v1/users/{id}"})
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @ApiOperation(value = "Delete User")
    @DeleteMapping("api/v1/users/{id}")
    public Long delete(@PathVariable Long id) {
        userService.delete(id);

        return id;
    }

}