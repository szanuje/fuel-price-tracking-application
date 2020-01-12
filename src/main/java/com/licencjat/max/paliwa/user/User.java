package com.licencjat.max.paliwa.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private Long id;
    private String login;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String role;
}
