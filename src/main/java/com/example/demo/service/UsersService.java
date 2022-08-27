package com.example.demo.service;

import com.example.demo.repository.Utenti_repository;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.security.AuthenticationRequest;
import com.example.demo.security.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsersService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private Utenti_repository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegistrationRequest data){
        T_Utente user = T_Utente.builder()
                .userName(data.getUserName())
                .password(passwordEncoder.encode(data.getPassword()))
                .nomeCognome(data.getNomeCognome())
                .build();

        userRepository.save(user);
    }

    public String authenticate(AuthenticationRequest data){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    data.getUsername(), data.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(data.getUsername());
        return jwtTokenService.generateToken(userDetails);
    }
}
