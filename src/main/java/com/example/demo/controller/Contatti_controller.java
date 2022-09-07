package com.example.demo.controller;

import com.example.demo.DTO.DTOcontatto;
import com.example.demo.eccezioni.*;

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

import java.util.ArrayList;
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

    private T_Contatto trovaContattoId(DTOcontatto dto, T_Utente utente){

            if (con_rep.existsT_ContattoByUtenteConAndIdContatto(utente, dto.getId())){
                return con_rep.getT_ContattoByUtenteConAndIdContatto(utente, dto.getId());
            }
            else{
                throw new NonTrovatoException("non esiste un contatto con id "+dto.getId()+" nelle rubrica dell'utente");
            }
        }
    private T_Contatto trovaContattoNome(DTOcontatto dto, T_Utente utente){
        T_Contatto contatto= null;
        if(con_rep.existsT_ContattoByUtenteConAndNomeCognome(utente, dto.getNomeCognome())){
            contatto =con_rep.getT_ContattoByUtenteConAndNomeCognome(utente, dto.getNomeCognome());
        }
        else{
            throw new NonTrovatoException("non esiste un contatto con nome "+dto.getNomeCognome()+" nelle rubrica dell'utente");
        }
        return contatto;
    }
    private List<T_Contatto> trovaContatti(DTOcontatto dto, T_Utente utente){
        List<T_Contatto> rispo = new ArrayList<>();
         if(dto.getId()!=null){
             rispo.add(trovaContattoId(dto, utente));

         } else if ((dto.getNomeCognome()!=null)&&(dto.getNomeCognome()!="")) {
             rispo.add(trovaContattoNome(dto, utente));

         }
         else if ((dto.getTipo()!= null)&&(dto.getTipo()!= "")) {
            if(tipi_contatto_repository.existsT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo())){
                T_Tipo_contatto tipo = tipi_contatto_repository.getT_Tipo_contattoByUtenteTcAndTipo(utente, dto.getTipo());
                rispo= con_rep.findT_ContattoByUtenteConAndTipoContattoCon(utente, tipo);
            }
            else{
                throw new NonTrovatoException("non esistono contatti con tipo "+dto.getTipo()+" nelle rubrica dell'utente");
            }
        }
         else if((dto.getEmail()!= null)&&(dto.getEmail()!="")){
             if(con_rep.existsT_ContattoByUtenteConAndAndEmail(utente, dto.getEmail())){
                 rispo = con_rep.findT_ContattoByUtenteConAndEmail(utente, dto.getEmail());
             }else {
                throw new NonTrovatoException("non esiste un contatto con mail "+dto.getEmail()+" in rubrica");
             }
         } else if ((dto.getTelefono() != null)&&(dto.getTelefono()!= "") ){
             if(con_rep.existsT_ContattoByUtenteConAndTelefono(utente, dto.getTelefono())){
                 rispo = con_rep.findT_ContattoByUtenteConAndTelefono(utente, dto.getTelefono());
             }else {
                 throw new NonTrovatoException("non esiste un contatto con numero di telefono: "+dto.getTelefono()+" in rubrica");
             }
         } else {
            rispo= con_rep.findT_ContattoByUtenteCon(utente);
        }
         return rispo;
    }





    @PostMapping
    public List<T_Contatto> gestioneContatto(@RequestBody DTOcontatto dtoContatto,
    @RequestHeader("Authorization") String Token){

        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        List<T_Contatto> ritorno = null;
        switch (dtoContatto.getCRUD()){
            case 'C':
                ritorno = postContatto(dtoContatto,utente);
                break;

            case 'R':
                ritorno = trovaContatti(dtoContatto, utente);
                break;
            case 'U':
                ritorno = modificaContatto(dtoContatto, utente);
                break;


            case 'D':
                ritorno = eliminaContatto(dtoContatto, utente);
                break;

        }
        return ritorno;

    }
    private List<T_Contatto> postContatto(DTOcontatto dtoContatto, T_Utente utente) {
        List<T_Contatto> risposta = new ArrayList<>();
        T_Contatto contatto = new T_Contatto();
        if(con_rep.existsT_ContattoByUtenteConAndNomeCognome(utente, dtoContatto.getNomeCognome())){
            throw new DoppioneException("esiste già un contatto avente per nome "+dtoContatto.getNomeCognome()+" in rubrica");
        }
        contatto = contattiService.inserisciContatto(dtoContatto, utente);
        risposta.add(contatto);
        return risposta;
    }

    private List<T_Contatto> modificaContatto(DTOcontatto dtoContatto, T_Utente utente){
        List<T_Contatto> risposta = new ArrayList<>();
        T_Contatto target= trovaContattoId(dtoContatto, utente);
        risposta.add(contattiService.aggiornaContatto(target, dtoContatto));
        return risposta;
    }
    private List<T_Contatto> eliminaContatto(DTOcontatto dto, T_Utente utente){
        List<T_Contatto> risposta = new ArrayList<>();
        T_Contatto target= trovaContattoId(dto, utente);
        con_rep.delete(target);
        return risposta;
    }



}
