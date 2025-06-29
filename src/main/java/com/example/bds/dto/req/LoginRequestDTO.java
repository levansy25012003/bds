package com.example.bds.dto.req;


import lombok.*;

@Getter
@Setter
public class LoginRequestDTO {
    private String fullname;
    private String avatar;
    private String email;
    private String password;
}
