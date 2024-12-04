package com.example.devjobs.user.entity;

import com.example.devjobs.user.dto.request.auth.SignUpRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@Table(name = "user")
public class User {

    @Id
    private String userId;
    private String password;
    private String email;
    private String type;
    private String role;

    public User(SignUpRequestDto dto){
        this.userId = dto.getId();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.type = "dev";
        this.role = "ROLE_USER";
    }

    public User(String userId, String email, String type){
        this.userId = userId;
        this.password = "passw0rd";
        this.email = email;
        this.type = type;
        this.role = "ROLE_USER";
    }
}