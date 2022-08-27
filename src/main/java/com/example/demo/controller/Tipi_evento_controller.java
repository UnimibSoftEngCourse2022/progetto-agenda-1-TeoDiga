package com.example.demo.controller;

import com.example.demo.DTO.DTOtipoEvento;
import com.example.demo.eccezioni.DoppioneException;
import com.example.demo.eccezioni.NonTrovatoException;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Tipi_evento_repository;
import com.example.demo.model.T_Tipo_evento;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.service.JwtTokenService;
import com.example.demo.service.TipiEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<T_Tipo_evento> getTipiEvento(@RequestHeader("Authorization") String Token){
        String username = jwtTokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        return tipi_evento_repository.findT_Tipo_eventoByUtenteTe(utente);
    }
    @GetMapping("/tipo:{tipo}")
    public T_Tipo_evento getTipoEventoByUtenteAndTipo(@RequestHeader("Authorization") String Token,
                                                      @PathVariable String tipo){
        String username = jwtTokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        if(tipi_evento_repository.existsByUtenteTeAndTipo(utente, tipo)) {

            return tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndTipo(utente, tipo);
        }
        else{
            throw new NonTrovatoException("non esiste un tipo evento "+tipo+" per l'utente "+utente.getUserName());
        }

    }
    @GetMapping("/id:{id}")
    public T_Tipo_evento getTipoEventoByUtenteAndTipo(@RequestHeader("Authorization") String Token,
                                                      @PathVariable Integer id){
        String username = jwtTokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        if(tipi_evento_repository.existsByUtenteTeAndIdTipoEvento(utente, id)) {
            return tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndIdTipoEvento(utente, id);
        }
        else{
            throw new NonTrovatoException("non esiste un tipo evento con id "+id+" per l'utente "+utente.getUserName());
        }

    }
    @PostMapping
    private T_Tipo_evento postTipoEvento (@RequestBody DTOtipoEvento dto,
                                                         @RequestHeader("Authorization") String Token)
                                         throws DoppioneException {
        String username = jwtTokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        if (tipi_evento_repository.existsByUtenteTeAndTipo(utente, dto.getTipo())) {
            throw new DoppioneException("tipo evento "+dto.getTipo()+" già esistente per l'utente "+utente.getUserName());
        } else {
            return tipiEventoService.inserisciTipoEvento(utente, dto);
        }
    }



}
