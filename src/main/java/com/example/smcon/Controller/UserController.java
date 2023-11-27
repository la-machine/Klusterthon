package com.example.smcon.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.smcon.DTO.UserDto;
import com.example.smcon.Service.LeadUserService;

@RestController
@RequestMapping("api/")
public class UserController {
    @Autowired
    private LeadUserService empService;

    @GetMapping("all")
    public ResponseEntity<?> getAllUsers(){
        return empService.getAllUsers();
    }
    

    @GetMapping(value = {"/{email}"})
    public ResponseEntity<?> getByEmail(@PathVariable String email){
        return empService.getByEmail(email);
    }
    
    @PostMapping("update")
    public ResponseEntity<?> updateEmployee(@RequestBody UserDto user){
        return empService.updateUser(user);
    }
}
