package com.sandbox.ProductSecurity.controller;

import com.sandbox.ProductSecurity.model.User;
import com.sandbox.ProductSecurity.repository.RoleRepository;
import com.sandbox.ProductSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public List<User> test1(){
        List<User> list = userRepository.findAll();
        return list;
    }
}
