package com.example.demo.repository;

import com.example.demo.model.T_Tipo_evento;
import com.example.demo.model.T_Utente;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tipi_evento_repository extends JpaRepository<T_Tipo_evento, Integer> {
    List<T_Tipo_evento> findT_Tipo_eventoByUtenteTe(T_Utente utente);
    T_Tipo_evento getT_Tipo_eventoByUtenteTeAndTipo(T_Utente utente, String tipo);
    T_Tipo_evento getT_Tipo_eventoByUtenteTeAndIdTipoEvento(T_Utente utente, Integer id);
    boolean existsByUtenteTeAndTipo(T_Utente utente, String tipo);
    boolean existsByUtenteTeAndIdTipoEvento(T_Utente utente, Integer id);
}
