package com.example.smcon.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.smcon.DTO.AuthRequest;
import com.example.smcon.DTO.UserDto;
import com.example.smcon.Service.LeadUserService;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    private LeadUserService empService;

    @PostMapping("add")
    public ResponseEntity<?> addUser(@RequestBody UserDto user){
        return empService.createUser(user);
    }

    @PostMapping()
    public String authenticate(@RequestBody AuthRequest authRequest){
        return empService.authenticate(authRequest);
    }

    @PostMapping("verify")
    public ResponseEntity<?> getVerifyemail(@RequestParam String email, @RequestParam String code){
        return empService.verifyUser(email, code);
    }
}
