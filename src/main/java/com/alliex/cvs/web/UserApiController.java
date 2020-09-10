package com.alliex.cvs.web;

import com.alliex.cvs.service.UserService;
import com.alliex.cvs.web.dto.UserResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
/*
    @ApiOperation(value = "Create User")
    @PostMapping("api/v1/users")
    public Long save(@RequestBody PostsSaveRequest postsSaveRequest) {
        return userService.save(postsSaveRequest);
    }

    @ApiOperation(value = "Update User")
    @PutMapping("api/v1/users/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequest request) {
        return userService.update(id, request);
    }
*/

    @ApiOperation(value = "Get Users")
    @GetMapping("/web-api/v1/users")
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }
/*

    @ApiOperation(value = "Get User")
    @GetMapping("api/v1/users/{id}")
    public PostsResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @ApiOperation(value = "Delete User")
    @DeleteMapping("api/v1/users/{id}")
    public Long delete(@PathVariable Long id) {
        userService.delete(id);

        return id;
    }
*/

}
