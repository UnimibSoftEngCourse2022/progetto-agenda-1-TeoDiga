package com.example.demo.controller;

import com.example.demo.model.T_Utente;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.service.Utenti_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utenti")


public class Utenti_controller {
    @Autowired
    private Utenti_repository ut_rep;
    @Autowired
    private Utenti_service service;
    @GetMapping
    public List<T_Utente> getUtenti(){
        return ut_rep.findAll();
    }

    @GetMapping("/{id}")
    public List<T_Utente> getUtenteById(@PathVariable Integer id){
        return ut_rep.findT_UtenteByIdUtente(id);
    }
    @GetMapping("/un:{username}")
    public List<T_Utente> getUtenteByUsername(@PathVariable String username){
        return ut_rep.findT_UtenteByUserName(username);
    }
    @PostMapping
    public T_Utente postUtente(@RequestBody T_Utente utente){
        return service.inserisciUtente(utente);
    }

}
