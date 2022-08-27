package com.example.demo.repository;

import com.example.demo.model.T_Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Utenti_repository extends JpaRepository <T_Utente, Integer> {


    List<T_Utente> findT_UtenteByIdUtente(Integer id_utente);
    T_Utente getT_UtenteByIdUtente(Integer id_utente);
    List<T_Utente> findT_UtenteByUserName(String username);
    T_Utente getT_UtenteByUserName(String username);
}
