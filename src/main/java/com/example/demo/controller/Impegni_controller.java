package com.example.demo.controller;

import com.example.demo.DTO.DTOImpegno;
import com.example.demo.eccezioni.FormatoNonValidoException;
import com.example.demo.eccezioni.NonTrovatoException;
import com.example.demo.model.T_Tipo_evento;
import com.example.demo.model.T_UMT;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Impegni_repository;
import com.example.demo.model.T_Impegno;
import com.example.demo.repository.Tipi_evento_repository;
import com.example.demo.repository.UMT_repository;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.service.EventiService;
import com.example.demo.service.ImpegniService;
import com.example.demo.service.ImpegniService;
import com.example.demo.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/impegni")
public class Impegni_controller {
    @Autowired
    private Impegni_repository impegni_repository;
    @Autowired
    private Utenti_repository utenti_repository;
    @Autowired
    private JwtTokenService tokenService;
    @Autowired
    private Tipi_evento_repository tipi_evento_repository;
    @Autowired
    private UMT_repository umt_repository;
    @Autowired
    private ImpegniService impegniService;
    @Autowired
    private EventiService ev;

    @GetMapping
    public List<T_Impegno> getImpegni(@RequestHeader("Authorization") String Token) {
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        return impegni_repository.findT_ImpegnoByUtenteImp(utente);
    }

    @GetMapping("/tipo:{tipo}")
    public List<T_Impegno> getImpegniByTipo(@RequestHeader("Authorization") String Token, @PathVariable String tipo) {
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        if (tipi_evento_repository.existsByUtenteTeAndTipo(utente, tipo)) {
            T_Tipo_evento tipo_evento = tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndTipo(utente, tipo);
            return impegni_repository.findT_ImpegnoByUtenteImpAndTipoEventoImp(utente, tipo_evento);
        } else {
            throw new NonTrovatoException("non esiste il tipo di impegno " + tipo + " per l'utente " + utente.getUserName());
        }
    }

    @PostMapping
    public T_Impegno postImpegno(@RequestBody DTOImpegno dto,
                                 @RequestHeader("Authorization") String Token) throws SQLException {
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        T_UMT freq, durata, all;
        freq = umt_repository.getT_UMTByIdUMT(dto.getFreq());
        durata = umt_repository.getT_UMTByIdUMT(dto.getDurata());
        all = umt_repository.getT_UMTByIdUMT(dto.getAll());

        T_Impegno IM = impegniService.inserisciImpegno(dto, utente, freq, durata, all);
        ev.inserisciEvento(IM, IM.getInizio_impegno());
        return IM;

    }

    @PutMapping
    public T_Impegno modificaImpegno(@RequestBody DTOImpegno dto,
                                     @RequestHeader("Authorization") String Token) throws SQLException {
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);

        return impegniService.aggiornaImpegno();
    }

}
