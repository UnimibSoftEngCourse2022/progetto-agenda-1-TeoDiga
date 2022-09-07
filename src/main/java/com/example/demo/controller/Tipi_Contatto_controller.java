package com.example.demo.controller;
import com.example.demo.DTO.DTOtipoContatto;
import com.example.demo.eccezioni.*;
import com.example.demo.model.T_Tipo_contatto;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Tipi_contatto_repository;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.service.JwtTokenService;
import com.example.demo.service.TipiContattoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    private T_Tipo_contatto trovaTipoId(DTOtipoContatto dto, T_Utente utente){
        if(tcon_rep.existsT_Tipo_contattoByUtenteTcAndIdTipoContatto(utente, dto.getId())){
            return tcon_rep.getT_Tipo_contattoByUtenteTcAndIdTipoContatto(utente, dto.getId());
        }
        else{
            throw new NonTrovatoException("non esiste un tipo contatto avente per id "+dto.getId()+" nella rubrica");
        }
    }
    private T_Tipo_contatto trovaTipoNome(DTOtipoContatto dto, T_Utente utente){
        if(tcon_rep.existsT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo())){
            return tcon_rep.getT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo());
        }
        else{
            throw new NonTrovatoException("non esiste un tipo contatto chiamato "+dto.getTipo()+" nella rubrica");
        }
    }
    private List<T_Tipo_contatto> trovaTipo(DTOtipoContatto dtOtipoContatto, T_Utente utente){
        List<T_Tipo_contatto> rispo = new ArrayList<>();
        if(dtOtipoContatto.getId()!=null){
           rispo.add(trovaTipoId(dtOtipoContatto, utente));
           return rispo;
        } else if ((dtOtipoContatto.getTipo()!= "")&&(dtOtipoContatto.getTipo()!=null)) {
            rispo.add(trovaTipoNome(dtOtipoContatto, utente));
            return rispo;
        } else {
            return tcon_rep.findT_Tipo_contattoByUtenteTc(utente);
        }
    }


    private List<T_Tipo_contatto> postTipoContatto(DTOtipoContatto dtOtipoContatto, T_Utente utente){


        List<T_Tipo_contatto> output= new ArrayList<>();

        if (tcon_rep.existsT_Tipo_contattoByUtenteTcAndTipo(utente, dtOtipoContatto.getTipo())) {
            throw new DoppioneException("tipo contatto "+dtOtipoContatto.getTipo()+" già esistente per l'utente "+utente.getUserName());
        } else {
           output.add(tipiContattoService.inserisciTipoContatto(dtOtipoContatto, utente));
        }
        return output;
    }



    @PostMapping
    public List<T_Tipo_contatto> gestioneTipiContatto(@RequestBody DTOtipoContatto dto,
                                                     @RequestHeader("Authorization") String Token){
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente  utente = utenti_repository.getT_UtenteByUserName(username);
        List<T_Tipo_contatto> ritorno= null;
        switch (dto.getCRUD()){
            case 'C':
                ritorno = postTipoContatto(dto,utente);
                break;
            case 'R':
                ritorno = trovaTipo(dto, utente);
                break;
            case 'U':
                ritorno = modificaTipoContatto(dto, utente);
                break;
            case 'D':
                ritorno = eliminaTipoContatto(dto, utente);
                break;
        }
        return ritorno;
    }
    private  List<T_Tipo_contatto> modificaTipoContatto( DTOtipoContatto dto,
                                                         T_Utente utente){

        List<T_Tipo_contatto> rispo= new ArrayList<>();
        T_Tipo_contatto target= trovaTipoId(dto, utente);
        tipiContattoService.aggiornaTipo(dto, target);
        rispo.add(target);
        return rispo;
    }
    private List<T_Tipo_contatto> eliminaTipoContatto(DTOtipoContatto dto, T_Utente utente){
        List<T_Tipo_contatto> rispo = new ArrayList<>();
        T_Tipo_contatto target = trovaTipoId(dto, utente);
        tcon_rep.delete(target);
        return rispo;
    }
}
