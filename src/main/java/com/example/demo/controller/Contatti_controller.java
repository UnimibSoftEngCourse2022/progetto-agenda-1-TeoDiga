package com.example.demo.controller;

import com.example.demo.DTO.DTOcontatto;
import com.example.demo.eccezioni.DoppioneException;
import com.example.demo.eccezioni.NonTrovatoException;
import com.example.demo.service.ContattiService;
import com.example.demo.model.T_Tipo_contatto;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Contatti_repository;
import com.example.demo.model.T_Contatto;
import com.example.demo.repository.Tipi_contatto_repository;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.service.JwtTokenService;
import com.example.demo.service.TipiContattoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contatti")


public class Contatti_controller {
    @Autowired
    private JwtTokenService tokenService;
    @Autowired
    private TipiContattoService tipiContattoService;
    @Autowired
    private Contatti_repository con_rep;
    @Autowired
    private Utenti_repository utenti_repository;
    @Autowired
    private Tipi_contatto_repository tipi_contatto_repository;
    @Autowired
    private ContattiService contattiService;
    @PostMapping
    public List<T_Contatto> getContatti(@RequestBody DTOcontatto dto,
                                        @RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente  utente = utenti_repository.getT_UtenteByUserName(username);

        if (dto.getId()!= null) {
            if (con_rep.existsT_ContattoByUtenteConAndIdContatto(utente, dto.getId())){
                return con_rep.findT_ContattoByUtenteConAndIdContatto(utente, dto.getId());
            }
            else{
                throw new NonTrovatoException("non esiste un contatto con id "+dto.getId()+" nelle rubrica dell'utente");
            }
        } else if (dto.getNomeCognome()!= null) {
            if(con_rep.existsT_ContattoByUtenteConAndNomeCognome(utente, dto.getNomeCognome())){
                return con_rep.findT_ContattoByUtenteConAndNomeCognome(utente, dto.getNomeCognome());
            }
            else{
                throw new NonTrovatoException("non esiste un contatto con nome "+dto.getNomeCognome()+" nelle rubrica dell'utente");
            }
        } else if (dto.getTipo()!= null) {
            if(tipi_contatto_repository.existsT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo())){
                T_Tipo_contatto tipo = tipi_contatto_repository.getT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo());
                return con_rep.findT_ContattoByUtenteConAndTipoContattoCon(utente, tipo);
            }
            else{
                throw new NonTrovatoException("non esistono contatti con tipo "+dto.getTipo()+" nelle rubrica dell'utente");
            }
        }
        else {
            return con_rep.findT_ContattoByUtenteCon(utente);
        }
    }




/*
    @PostMapping
    public T_Contatto postContatto(@RequestBody DTOcontatto dtoContatto,
                                   @RequestHeader("Authorization") String Token) {

        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));

        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        T_Contatto contatto = new T_Contatto();
        contatto = contattiService.inserisciContatto(dtoContatto, utente);
        return contatto;
    }

 */
    @PutMapping()
    public  T_Contatto modificaContatto(@RequestBody DTOcontatto dtoContatto,
            @RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente  utente = utenti_repository.getT_UtenteByUserName(username);
        T_Contatto target= null;
        if(con_rep.existsT_ContattoByUtenteConAndIdContatto(utente, dtoContatto.getId())){
            target=con_rep.getT_ContattoByUtenteConAndIdContatto(utente, dtoContatto.getId());
        } else if(con_rep.existsT_ContattoByUtenteConAndNomeCognome(utente, dtoContatto.getNomeCognome())) {
             target =con_rep.getT_ContattoByUtenteConAndNomeCognome(utente, dtoContatto.getNomeCognome());
        }
        else {
            throw new NonTrovatoException("non esiste un contatto chiamato " + dtoContatto.getNomeCognome() + " nella rubrica dell'utente " + utente.getUserName());
        }

        return contattiService.aggiornaContatto(target, dtoContatto);
    }


}
