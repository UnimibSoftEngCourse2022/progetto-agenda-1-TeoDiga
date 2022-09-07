package com.example.demo.controller;
import com.example.demo.model.*;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.repository.Eventi_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.EventiService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.example.demo.eccezioni.*;
import com.example.demo.DTO.DTOEvento;
import com.example.demo.service.JwtTokenService;
import com.example.demo.repository.Impegni_repository;
import com.example.demo.repository.Attivita_repository;
import com.example.demo.repository.Tipi_evento_repository;

@RestController
@RequestMapping("/api/eventi")
public class Eventi_controller {
    @Autowired
    private Tipi_evento_repository tipi_evento_repository;
    @Autowired
    private Attivita_repository attivita_repository;
    @Autowired
    private Impegni_repository impegni_repository;
    @Autowired
    private Eventi_repository ev_rep;
    @Autowired
    private EventiService es;
    @Autowired
    private Utenti_repository utenti_repository;
    @Autowired
    private JwtTokenService tokenService;
    private T_Evento trovaEventoId(DTOEvento dto, T_Utente utente){
        if (ev_rep.existsT_EventoByUtenteEvAndIdEvento(utente, dto.getId())){
            return ev_rep.getT_EventoByUtenteEvAndIdEvento(utente, dto.getId());
        }
        else{
            throw new NonTrovatoException("non esiste un evento con id "+dto.getId()+" nelle rubrica dell'utente");
        }
    }
    private List<T_Evento> trovaEventiI(DTOEvento dto, T_Utente utente){
        List<T_Evento> ritorno = new ArrayList<>();
        if(dto.getImp()!=null){
            T_Impegno imp=null;
            if (impegni_repository.existsT_ImpegnoByUtenteImpAndIdImpegno(utente, dto.getImp())){
                imp= impegni_repository.getT_ImpegnoByUtenteImpAndIdImpegno(utente, dto.getImp());
                ritorno=ev_rep.findT_EventoByUtenteEvAndImpegnoOrderByOrarioInizioAsc(utente, imp);
            }
            else{
                throw new NonTrovatoException("placeholder imbecille");
            }

        }
        return  ritorno;
    }
    private List<T_Evento> trovaEventiA(DTOEvento dto, T_Utente utente){
        List<T_Evento> ritorno = new ArrayList<>();
        if(dto.getAtt()!=null){
            T_Attivita att=null;
            if (attivita_repository.existsT_AttivitaByUtenteAttAndIdAttivita(utente, dto.getAtt())){
                att= attivita_repository.getT_AttivitaByUtenteAttAndIdAttivita(utente, dto.getAtt());
                ritorno=ev_rep.findT_EventoByUtenteEvAndAttivita(utente, att);
            }
            else{
                throw new NonTrovatoException("placeholder imbecille");
            }

        }
        return  ritorno;
    }
    private List<T_Evento> trovaEventi(DTOEvento dto, T_Utente utente){
        List<T_Evento> ritorno = new ArrayList<>();
        if(dto.getId()!= null){
            T_Evento te = trovaEventoId(dto, utente);
            ritorno.add(te);
        }

        String parTitolo = "%";
        if (dto.getTitolo()!=null && dto.getTitolo()!="" ) {parTitolo = dto.getTitolo();}

        Integer parTipoMin = 0;
        Integer parTipoMax = 999999999;
        if(dto.getTipo()!= null && dto.getTipo()!="" )  {
            T_Tipo_evento te =tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndTipo(utente, dto.getTipo());
            parTipoMin = te.getIdTipoEvento();
            parTipoMax = te.getIdTipoEvento();
        }
        Timestamp parTSDal = Timestamp.valueOf("1000-01-01 00:00:00");
        if(dto.getInizio()!=null && dto.getInizio()!="") {parTSDal=Timestamp.valueOf(dto.getInizio().replace("T"," ")+":00");}

        Timestamp parTSAl = Timestamp.valueOf("3000-12-31 00:00:00");
        if(dto.getFine()!=null && dto.getFine()!="") {parTSAl=Timestamp.valueOf(dto.getFine().replace("T"," ")+":00");}
        List<Integer> listaIdEv=ev_rep.filtraEventi(parTitolo, parTipoMin, parTipoMax, parTSDal, parTSAl, utente.getIdUtente());
        Iterator<Integer> i =listaIdEv.iterator();
        while (i.hasNext()){
            ritorno.add(ev_rep.getT_EventoByUtenteEvAndIdEvento(utente, i.next()));
        }
        return ritorno;
    }




    @PostMapping
    public List<T_Evento> gestisciEventi(@RequestBody DTOEvento dto,
                                         @RequestHeader("Authorization") String Token)throws SQLException{
        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        List<T_Evento> ritorno= new ArrayList<>();
        switch (dto.getCRUD()) {
            case 'A':
                ritorno = trovaEventiA(dto, utente);
                break;
            case 'I':
                ritorno = trovaEventiI(dto, utente);
                break;
            case 'R':
                ritorno = trovaEventi(dto, utente);
                break;
            case 'U':
                ritorno = modificaEvento(dto, utente);
                break;
            case 'D':
                ritorno = eliminaEvento(dto, utente);
                break;
        }
        return ritorno;
    }
    private List<T_Evento> eliminaEvento(DTOEvento dto, T_Utente utente){
        List<T_Evento> ritorno = null;
        T_Evento target = trovaEventoId(dto, utente);
        if(target.getAttivita()!=null){
            T_Attivita att= target.getAttivita();
            att.setPianificata(false);
        }
        ev_rep.delete(target);

        return ritorno;
    }
    private List<T_Evento> modificaEvento(DTOEvento dto, T_Utente utente) throws SQLException {
        List<T_Evento> ritorno= new ArrayList<>();

        T_Evento target = ev_rep.getT_EventoByUtenteEvAndIdEvento(utente, dto.getId());
        es.aggiornaEvento(target, dto);
        ritorno.add(target);
        return ritorno;
    }


}
