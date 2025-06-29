package com.example.bds.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginGoogleDTO {
    private String fullname;
    private String avatar;
    private String email;
    private String password;
}
