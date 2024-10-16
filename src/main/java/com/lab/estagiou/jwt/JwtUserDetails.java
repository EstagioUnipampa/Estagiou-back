package com.lab.estagiou.jwt;

import java.util.UUID;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.lab.estagiou.model.user.UserEntity;

public class JwtUserDetails extends User {

    private UserEntity usuario;

    public JwtUserDetails(UserEntity usuario) {
        super(usuario.getEmail(), usuario.getPassword(),
                AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }

    public UUID getId() {
        return usuario.getId();
    }

    public String getRole() {
        return usuario.getRole().name();
    }

}
