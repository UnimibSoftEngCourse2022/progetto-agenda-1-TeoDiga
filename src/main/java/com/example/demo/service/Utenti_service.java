package com.example.demo.service;

import com.example.demo.model.T_Utente;
import com.example.demo.repository.Utenti_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Utenti_service {
    @Autowired
    private Utenti_repository utenti_repository;

    public T_Utente inserisciUtente(T_Utente utente){
        return utenti_repository.save(utente);
    }
}
