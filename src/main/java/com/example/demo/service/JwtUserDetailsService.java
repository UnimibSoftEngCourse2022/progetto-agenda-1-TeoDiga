package com.example.demo.service;

import com.example.demo.model.T_Utente;
import com.example.demo.repository.Utenti_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_" + USER;

    @Autowired
    private Utenti_repository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        T_Utente user = userRepository.findT_UtenteByUserName(username).get(0);
        return new User(user.getUserName(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
    }
}