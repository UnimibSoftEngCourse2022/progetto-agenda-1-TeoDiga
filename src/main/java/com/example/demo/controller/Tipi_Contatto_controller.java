package com.example.demo.controller;
import com.example.demo.DTO.DTOtipoContatto;
import com.example.demo.eccezioni.DoppioneException;
import com.example.demo.eccezioni.NonTrovatoException;
import com.example.demo.model.T_Tipo_contatto;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Tipi_contatto_repository;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.service.JwtTokenService;
import com.example.demo.service.TipiContattoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import  java.util.List;

@RestController
@RequestMapping("/api/tipi_contatto")
public class Tipi_Contatto_controller {
    @Autowired
    private JwtTokenService tokenService;
    @Autowired
    private Tipi_contatto_repository tcon_rep;
    @Autowired
    private Utenti_repository utenti_repository;
    @Autowired
    private TipiContattoService tipiContattoService;
    @GetMapping
    public List<T_Tipo_contatto> getTipiContatto(@RequestBody DTOtipoContatto dtOtipoContatto,
                                                 @RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente  utente = utenti_repository.getT_UtenteByUserName(username);
        return trovaTipo(dtOtipoContatto, utente);

    }
    private List<T_Tipo_contatto> trovaTipo(DTOtipoContatto dtOtipoContatto, T_Utente utente){
        if(dtOtipoContatto.getId()!=null){
            if(tcon_rep.existsT_Tipo_contattoByUtenteTcAndIdTipoContatto(utente, dtOtipoContatto.getId())){
                return tcon_rep.findT_Tipo_contattoByUtenteTcAndIdTipoContatto(utente, dtOtipoContatto.getId());
            }
            else{
                throw new NonTrovatoException("non esiste un tipo contatto avente per id "+dtOtipoContatto.getId()+" nella rubrica");
            }
        } else if (dtOtipoContatto.getTipo()!= null) {
            if(tcon_rep.existsT_Tipo_contattoByUtenteTcAndTipo(utente, dtOtipoContatto.getTipo())){
                return tcon_rep.findT_Tipo_contattoByUtenteTcAndTipo(utente, dtOtipoContatto.getTipo());
            }
            else{
                throw new NonTrovatoException("non esiste un tipo contatto chiamato "+dtOtipoContatto.getTipo()+" nella rubrica");
            }
        } else {
            return tcon_rep.findT_Tipo_contattoByUtenteTc(utente);
        }
    }

    @PostMapping
    private T_Tipo_contatto postTipoContatto(@RequestBody DTOtipoContatto dtOtipoContatto,
                                             @RequestHeader("Authorization") String Token){

        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente  utente = utenti_repository.getT_UtenteByUserName(username);
        if (tcon_rep.existsT_Tipo_contattoByUtenteTcAndTipo(utente, dtOtipoContatto.getTipo())) {
            throw new DoppioneException("tipo contatto "+dtOtipoContatto.getTipo()+" già esistente per l'utente "+utente.getUserName());
        } else {
            return tipiContattoService.inserisciTipoContatto(dtOtipoContatto, utente);
        }
    }
    @PutMapping
    public T_Tipo_contatto modificaTipoContatto(@RequestBody DTOtipoContatto dto,
                                                @RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente  utente = utenti_repository.getT_UtenteByUserName(username);
        T_Tipo_contatto target= null;
        if(tcon_rep.existsT_Tipo_contattoByUtenteTcAndIdTipoContatto(utente, dto.getId())){
            target = tipiContattoService.aggiornaTipo(dto, tcon_rep.getT_Tipo_contattoByUtenteTcAndIdTipoContatto(utente, dto.getId()));
        }
        if (tcon_rep.existsT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo())) {
            target = tipiContattoService.aggiornaTipo(dto, tcon_rep.getT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo()));
         } else {
            throw new NonTrovatoException("tipo contatto "+dto.getTipo()+" non esite per l'utente "+utente.getUserName());
        }
        return target;
    }
}
