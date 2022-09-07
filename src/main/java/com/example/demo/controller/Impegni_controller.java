package com.example.demo.controller;

import com.example.demo.DTO.DTOImpegno;
import com.example.demo.eccezioni.FormatoNonValidoException;
import com.example.demo.eccezioni.NonTrovatoException;
import com.example.demo.model.*;
import com.example.demo.repository.Eventi_repository;
import com.example.demo.repository.Impegni_repository;
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
import java.util.ArrayList;
import java.util.Iterator;
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
    @Autowired
    private Eventi_repository eventi_repository;

    private T_Impegno trovaImpegnoId(DTOImpegno dto, T_Utente utente){

        if (impegni_repository.existsT_ImpegnoByUtenteImpAndIdImpegno(utente, dto.getId())){
            return impegni_repository.getT_ImpegnoByUtenteImpAndIdImpegno(utente, dto.getId());
        }
        else{
            throw new NonTrovatoException("non esiste un impegno con id "+dto.getId()+" nelle rubrica dell'utente");
        }
    }
    private List<T_Impegno> trovaImpegnoNome(DTOImpegno dto, T_Utente utente){
        List<T_Impegno> rispo = new ArrayList<>();
        if(impegni_repository.existsT_ImpegnoByUtenteImpAndTitolo(utente, dto.getTitolo())){
            rispo = impegni_repository.findT_ImpegnoByUtenteImpAndTitolo(utente, dto.getTitolo());
        }
        else{
            throw new NonTrovatoException("non esiste un impegno con titolo "+dto.getTitolo()+" nelle rubrica dell'utente");
        }
        return rispo;
    }
    private List<T_Impegno> trovaImpegni(DTOImpegno dto, T_Utente utente){
        List<T_Impegno> rispo = new ArrayList<>();
        if(dto.getId()!= null){
            rispo.add(trovaImpegnoId(dto, utente));
        } else if ((dto.getTitolo()!= null)&&(dto.getTitolo()!="")) {
            rispo = trovaImpegnoNome(dto, utente);

        } else if ((dto.getTipo()!=null)&&(dto.getTipo()!="")&&(tipi_evento_repository.existsByUtenteTeAndTipo(utente, dto.getTipo()))) {
            T_Tipo_evento tipo= tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndTipo(utente, dto.getTipo());
            rispo = impegni_repository.findT_ImpegnoByUtenteImpAndTipoEventoImp(utente, tipo);
        } else {
            rispo = impegni_repository.findT_ImpegnoByUtenteImp(utente);
        }

        return rispo;
    }

     private List<T_Impegno> postImpegno(DTOImpegno dto,T_Utente utente) throws SQLException {
         List<T_Impegno> rispo = new ArrayList<>();


         T_Impegno IM = impegniService.inserisciImpegno(dto, utente);
         ev.inserisciEventoI(IM, IM.getInizioImpegno());
         rispo.add(IM);
         return rispo;}
    @PostMapping
    public List<T_Impegno> gestioneImpegno(@RequestBody DTOImpegno dto,
                                             @RequestHeader("Authorization") String Token) throws SQLException {

        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        List<T_Impegno> ritorno = null;
        switch (dto.getCRUD()){
            case 'C':
                ritorno = postImpegno(dto,utente);
                break;

            case 'R':
                ritorno = trovaImpegni(dto, utente);
                break;
            case 'U':
                ritorno = modificaImpegno(dto, utente);
                break;


            case 'D':
                ritorno = eliminaImpegno(dto, utente);
                break;

        }
        return ritorno;

    }
    private List<T_Impegno> modificaImpegno(DTOImpegno dto, T_Utente utente) throws SQLException{
        List<T_Impegno> risposta = new ArrayList<>();
        T_Impegno target= trovaImpegnoId(dto, utente);

        risposta.add(impegniService.aggiornaImpegno(target, dto));

        List<T_Evento> condannati= eventi_repository.findT_EventoByUtenteEvAndImpegnoOrderByOrarioInizioAsc(utente, target);
        Iterator<T_Evento> i= condannati.iterator();
        while(i.hasNext()){
            eventi_repository.delete(i.next());
        }
        ev.inserisciEventoI(target, target.getInizioImpegno());
        return risposta;
    }
    private List<T_Impegno> eliminaImpegno(DTOImpegno dto, T_Utente utente){
        List<T_Impegno> risposta = new ArrayList<>();
        T_Impegno target= trovaImpegnoId(dto, utente);
        impegni_repository.delete(target);

        return risposta;
    }

}
