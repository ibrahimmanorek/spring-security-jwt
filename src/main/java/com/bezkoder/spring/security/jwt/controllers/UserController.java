package com.bezkoder.spring.security.jwt.controllers;

import com.bezkoder.spring.security.jwt.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/all")
    public ResponseEntity<List<?>> getUsers(){
        return ResponseEntity.ok().body(userDetailsService.findAll());
    }
}
