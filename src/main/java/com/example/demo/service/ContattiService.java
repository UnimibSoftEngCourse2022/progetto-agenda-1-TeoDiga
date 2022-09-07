package com.example.demo.service;

import com.example.demo.DTO.DTOcontatto;
import com.example.demo.eccezioni.NonTrovatoException;
import com.example.demo.model.T_Contatto;
import com.example.demo.model.T_Tipo_contatto;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Contatti_repository;
import com.example.demo.repository.Tipi_contatto_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContattiService {
    @Autowired
    private Contatti_repository contatti_repository;
    @Autowired
    private Tipi_contatto_repository tipi_contatto_repository;
    public T_Contatto inserisciContatto(DTOcontatto dto, T_Utente utente){
        T_Contatto contatto = new T_Contatto();
        if( dto.getTipo()!= null){
            contatto.setTipoContattoCon(tipi_contatto_repository.getT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo()));
        }
        contatto.setUtenteCon(utente);
        contatto.setNomeCognome(dto.getNomeCognome());
        contatto.setEmail(dto.getEmail());
        contatto.setTelefono(dto.getTelefono());
        return contatti_repository.save(contatto);
    }

    public T_Contatto aggiornaContatto(T_Contatto contatto, DTOcontatto dto){
        T_Utente utente= contatto.getUtenteCon();
        if( dto.getTipo()!= null) {
            contatto.setTipoContattoCon(tipi_contatto_repository.getT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo()));
        }
        if(dto.getNomeCognome() != null) {
            contatto.setNomeCognome(dto.getNomeCognome());
        }
        if(dto.getEmail() != null) {
            contatto.setEmail(dto.getEmail());
        }
        if(dto.getTelefono() != null) {
            contatto.setTelefono(dto.getTelefono());
        }
        return contatti_repository.save(contatto);

    }


}
