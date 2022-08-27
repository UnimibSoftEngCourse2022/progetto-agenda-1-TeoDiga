package com.example.demo.controller;

import com.example.demo.security.AuthenticationRequest;
import com.example.demo.security.RegistrationRequest;
import com.example.demo.service.JwtTokenService;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping("/hello")
    public String hello(@RequestHeader (name="Authorization") String token){
        String username = jwtTokenService.validateTokenAndGetUsername(estraiToken(token));
        return "Ciao, " + username;
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthenticationRequest request){
       return usersService.authenticate(request);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegistrationRequest request){
        usersService.register(request);
    }

    private String estraiToken(String token){
        return token.substring(7);
    }

}
