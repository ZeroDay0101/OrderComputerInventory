package org.example.ecommerceorderandinventory.controller;

import jakarta.validation.Valid;
import org.example.ecommerceorderandinventory.dto.in.CreateUserDTO;
import org.example.ecommerceorderandinventory.mappers.UserMapper;
import org.example.ecommerceorderandinventory.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class RegisterController {


    private final UserService userService;


    public RegisterController(UserMapper userMapper, UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody @Valid CreateUserDTO createUserDTO) {

        userService.createUser(createUserDTO);

        return ResponseEntity.ok().body("User successfully created");
    }
}
