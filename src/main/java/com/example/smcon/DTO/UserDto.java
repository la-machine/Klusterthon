package com.example.smcon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String tel;
    private String referal;
}
