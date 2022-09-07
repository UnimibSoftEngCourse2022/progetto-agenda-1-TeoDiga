package com.example.demo.controller;

import com.example.demo.DTO.DTOtipoEvento;
import com.example.demo.eccezioni.*;
import com.example.demo.model.T_Contatto;
import com.example.demo.model.T_Tipo_contatto;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Tipi_evento_repository;
import com.example.demo.model.T_Tipo_evento;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.service.JwtTokenService;
import com.example.demo.service.TipiEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tipi_evento")
public class Tipi_evento_controller {
    @Autowired
    private Tipi_evento_repository tipi_evento_repository;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private Utenti_repository utenti_repository;
    @Autowired
    private TipiEventoService tipiEventoService;

    private T_Tipo_evento trovaTipoId(DTOtipoEvento dto, T_Utente utente){
        if(tipi_evento_repository.existsByUtenteTeAndIdTipoEvento(utente, dto.getId())){
            return tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndIdTipoEvento(utente, dto.getId());
        }
        else{
            throw new NonTrovatoException("non esiste un tipo di evento avente per id "+dto.getId());
        }
    }
    private T_Tipo_evento trovaTipoNome(DTOtipoEvento dto, T_Utente utente){
        if(tipi_evento_repository.existsByUtenteTeAndTipo(utente, dto.getTipo())){
            return tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndTipo(utente, dto.getTipo());
        }
        else{
            throw new NonTrovatoException("non esiste un tipo di evento avente per id "+dto.getTipo());
        }
    }
    private List<T_Tipo_evento> trovaTipoE(DTOtipoEvento dto, T_Utente utente){
        List<T_Tipo_evento> rispo = new ArrayList<>();
        if(dto.getId()!= null){
            rispo.add(trovaTipoId(dto, utente));
        } else if ((dto.getTipo()!= null)&&(dto.getTipo()!="")) {
            rispo.add(trovaTipoNome(dto, utente));
        }
        else{
            rispo = tipi_evento_repository.findT_Tipo_eventoByUtenteTe(utente);
        }
        return rispo;
    }
    private List<T_Tipo_evento> postTipoE(DTOtipoEvento dto, T_Utente utente){
        List<T_Tipo_evento> rispo = new ArrayList<>();
        T_Tipo_evento evento = new T_Tipo_evento();
        if(tipi_evento_repository.existsByUtenteTeAndTipo(utente, dto.getTipo())){
            throw new DoppioneException("nell'agenda c'è già un tipo di evento chiamato "+dto.getTipo());
        }
        evento = tipiEventoService.inserisciTipoEvento(utente, dto);
        rispo.add(evento);
        return rispo;
    }
    @PostMapping
    private List<T_Tipo_evento> gestisciTipoEvento (@RequestBody DTOtipoEvento dto,
                                                         @RequestHeader("Authorization") String Token)
                                         throws DoppioneException {
        String username = jwtTokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        List<T_Tipo_evento> ritorno = null;
        switch (dto.getCRUD()){
            case 'C':
                ritorno = postTipoE(dto,utente);
                break;

            case 'R':
                ritorno = trovaTipoE(dto, utente);
                break;
            case 'U':
                ritorno = modificaTipoE(dto, utente);
                break;


            case 'D':
                ritorno = eliminaTipoE(dto, utente);
                break;

        }
        return ritorno;
    }
    private List<T_Tipo_evento> modificaTipoE(DTOtipoEvento dto, T_Utente utente){
        List<T_Tipo_evento> risposta = new ArrayList<>();
        T_Tipo_evento target= trovaTipoId(dto, utente);
        risposta.add(tipiEventoService.aggiornaTipoE(target, dto));
        return risposta;
    }
    private List<T_Tipo_evento> eliminaTipoE(DTOtipoEvento dto, T_Utente utente){
        List<T_Tipo_evento> risposta = new ArrayList<>();
        T_Tipo_evento target= trovaTipoId(dto, utente);
        tipi_evento_repository.delete(target);
        return risposta;
    }



}
