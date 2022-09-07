package com.example.demo.service;

import com.example.demo.DTO.DTOtipoContatto;
import com.example.demo.model.T_Tipo_contatto;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Tipi_contatto_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipiContattoService {
    @Autowired
    private Tipi_contatto_repository tipi_contatto_repository;

    public T_Tipo_contatto inserisciTipoContatto(DTOtipoContatto dtoTipo, T_Utente utente){
        T_Tipo_contatto tipo= new T_Tipo_contatto();
        tipo.setUtenteTc(utente);
        tipo.setTipo(dtoTipo.getTipo());
        tipo.setDescrizione(dtoTipo.getDescrizione());
        tipo.setColore(dtoTipo.getColore());
        return tipi_contatto_repository.save(tipo);
    }

    public T_Tipo_contatto aggiornaTipo(DTOtipoContatto dto, T_Tipo_contatto tipo_contatto){
        if(dto.getTipo() != null){
                tipo_contatto.setTipo(dto.getTipo());
        }
        if(dto.getDescrizione() != null){
            tipo_contatto.setDescrizione(dto.getDescrizione());
        }
        if(dto.getColore()!= null){
            tipo_contatto.setColore(dto.getColore());
        }
        return tipi_contatto_repository.save(tipo_contatto);
    }

   }
