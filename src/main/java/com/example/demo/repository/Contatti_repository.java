package com.example.demo.repository;

import com.example.demo.model.T_Contatto;
import com.example.demo.model.T_Tipo_contatto;
import com.example.demo.model.T_Utente;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Contatti_repository extends JpaRepository <T_Contatto, Integer> {
    boolean existsT_ContattoByUtenteConAndAndEmail(T_Utente utente, String email);
    boolean existsT_ContattoByUtenteConAndTelefono(T_Utente utente, String telefono);
    List<T_Contatto> findT_ContattoByUtenteConAndEmail(T_Utente utente, String telefono);
    List<T_Contatto> findT_ContattoByUtenteConAndTelefono(T_Utente utente, String telefono);

    T_Contatto getT_ContattoByIdContatto(Integer id);
    List<T_Contatto> findT_ContattoByUtenteCon(T_Utente utente);
    List<T_Contatto> findT_ContattoByUtenteConAndTipoContattoCon(T_Utente utente, T_Tipo_contatto tipo);
    T_Contatto getT_ContattoByUtenteConAndIdContatto(T_Utente utente, Integer id);
    List<T_Contatto> findT_ContattoByUtenteConAndIdContatto(T_Utente utente, Integer id);
    boolean existsT_ContattoByUtenteConAndIdContatto(T_Utente utente, Integer id);
    T_Contatto getT_ContattoByUtenteConAndNomeCognome(T_Utente utente, String nomeCognome);
    boolean existsT_ContattoByUtenteConAndNomeCognome(T_Utente utente, String nomeCognome);
    List<T_Contatto> findT_ContattoByUtenteConAndNomeCognome(T_Utente utente, String nome);

}
