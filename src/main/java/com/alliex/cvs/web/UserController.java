package com.alliex.cvs.web;

import com.alliex.cvs.domain.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Controller
public class UserController {

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<String> roleNames = Stream.of(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        model.addAttribute("roleNames", roleNames);

        return "user";
    }

}
