package com.example.demo.controller;

import com.example.demo.DTO.DTOAttivita;
import com.example.demo.eccezioni.FormatoNonValidoException;
import com.example.demo.model.T_Attivita;
import com.example.demo.model.T_UMT;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Attivita_repository;
import com.example.demo.repository.Tipi_evento_repository;
import com.example.demo.repository.UMT_repository;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.service.AttivitaService;
import com.example.demo.service.EventiService;
import com.example.demo.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@RestController
@RequestMapping("/api/attivita")

public class Attivita_controller {
    @Autowired
    private Attivita_repository attivita_repository;
    @Autowired
    private Utenti_repository utenti_repository;
    @Autowired
    private JwtTokenService tokenService;
    @Autowired
    private Tipi_evento_repository tipi_evento_repository;
    @Autowired
    private UMT_repository umt_repository;
    @Autowired
    private AttivitaService attivitaService;
    @Autowired
    private EventiService eventiService;
    @GetMapping
    public List<T_Attivita> getAttivita(@RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        return attivita_repository.findT_AttivitaByUtenteAtt(utente);
    }

    @PostMapping
    public T_Attivita postAttivita(@RequestBody DTOAttivita dto,
                                   @RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        T_UMT durata, all;

        durata= umt_repository.getT_UMTByIdUMT(dto.getDurata());
        all= umt_repository.getT_UMTByIdUMT(dto.getAllarme());
        Timestamp prova;
        try {
            prova= Timestamp.valueOf(dto.getCreazione());
        } catch (IllegalArgumentException exception){
            throw new FormatoNonValidoException("formato dell'inizio dell'impegno non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
        }
        return attivitaService.inserisciAttivita(dto, utente, durata, all, prova);

    }
    @GetMapping("/hint")
    public T_Attivita  suggerisciAttivita(@RequestHeader("Authorization") String Token) throws SQLException {
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        Timestamp dal = attivita_repository.inizioPeriodoLibero(utente.getIdUtente());
        Timestamp al = attivita_repository.finePeriodoLibero(utente.getIdUtente(), dal);
        Integer idAtt = attivita_repository.trovaHint(utente.getIdUtente(), dal, al).get(0);
        T_Attivita rispo= attivita_repository.getT_AttivitaByUtenteAttAndIdAttivita(utente, idAtt);
        rispo.setCreazione(dal);
        Timestamp fine = eventiService.sommaTempi(dal, rispo.getUmtDurataAtt(),rispo.getDurataAN());
        rispo.setScadenza(fine);
        return rispo;
    }
    /*
    @PutMapping("/plan")
    public T_Attivita pianificaAttivita(@RequestBody DTOAttivita dto,
                                        @RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);

    }
    */
    @GetMapping("/max")
    public List<T_Attivita> massimizzaAttività(
            @RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        Timestamp dal = attivita_repository.inizioPeriodoLibero(utente.getIdUtente());
        Timestamp al = attivita_repository.finePeriodoLibero(utente.getIdUtente(), dal);

        List<Integer> ids = attivita_repository.trovaRiempitivo(dal, al, utente.getIdUtente());
        List<T_Attivita> rispo = new ArrayList<>();
        Iterator<Integer> i = ids.iterator();

        while (i.hasNext()) {
            rispo.add(attivita_repository.getT_AttivitaByUtenteAttAndIdAttivita(utente, i.next()));
        }
        return rispo;
    }

}
