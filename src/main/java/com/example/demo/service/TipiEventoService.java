package com.example.demo.service;

import com.example.demo.DTO.DTOtipoEvento;
import com.example.demo.model.T_Tipo_evento;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Tipi_evento_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipiEventoService {
    @Autowired
    private Tipi_evento_repository tipi_evento_repository;

    public T_Tipo_evento inserisciTipoEvento(T_Utente utente, DTOtipoEvento dtOtipoEvento){
        T_Tipo_evento tipo = new T_Tipo_evento();
        tipo.setUtenteTe(utente);
        tipo.setTipo(dtOtipoEvento.getTipo());
        tipo.setDescrizione(dtOtipoEvento.getDescrizione());
        tipo.setColore(dtOtipoEvento.getColore());
        return tipi_evento_repository.save(tipo);
    }
}
