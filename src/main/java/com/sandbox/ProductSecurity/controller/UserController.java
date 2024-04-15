package com.sandbox.ProductSecurity.controller;

import com.sandbox.ProductSecurity.model.User;
import com.sandbox.ProductSecurity.security.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/showReg")
    public String showRegistrationPage(){
        return "registerUser";
    }

    @PostMapping("/registerUser")
    public String register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return "login";
    }

    public String login(String email, String password, HttpServletRequest request, HttpServletResponse response){
        boolean loginResponse = securityService.login(email, password, request, response);
        if (loginResponse){
            return "index";
        } else {
            return "login";
        }
    }

}
