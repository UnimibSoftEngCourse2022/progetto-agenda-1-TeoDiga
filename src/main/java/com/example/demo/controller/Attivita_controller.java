package com.example.demo.controller;

import com.example.demo.Altro.SlotTemporale;
import com.example.demo.DTO.DTOAttivita;
import com.example.demo.eccezioni.*;
import com.example.demo.repository.Eventi_repository;
import com.example.demo.model.*;
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
import java.util.*;

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
    @Autowired
    private Eventi_repository er;
    private T_Attivita trovaAttivitaId(DTOAttivita dto, T_Utente utente){

        if (attivita_repository.existsT_AttivitaByUtenteAttAndIdAttivita(utente, dto.getId())){
            return attivita_repository.getT_AttivitaByUtenteAttAndIdAttivita(utente, dto.getId());
        }
        else{
            throw new NonTrovatoException("non esiste un attivita con id "+dto.getId()+" nelle rubrica dell'utente");
        }
    }
    private List<T_Attivita> trovaAttivitaNome(DTOAttivita dto, T_Utente utente){
        List<T_Attivita> rispo = new ArrayList<>();
        if(attivita_repository.existsT_AttivitaByUtenteAttAndTitolo(utente, dto.getTitolo())){
            rispo = attivita_repository.findT_AttivitaByUtenteAttAndTitolo(utente, dto.getTitolo());
        }
        else{
            throw new NonTrovatoException("non esiste un attività con titolo "+dto.getTitolo()+" nelle rubrica dell'utente");
        }
        return rispo;
    }
    private List<T_Attivita> trovaAttivita(DTOAttivita dto, T_Utente utente){
        List<T_Attivita> rispo = new ArrayList<>();
        if(dto.getId()!= null){
            rispo.add(trovaAttivitaId(dto, utente));
        } else if (dto.getTitolo()!= null) {
            rispo = trovaAttivitaNome(dto, utente);

        } else if (( dto.getTipo()!=null)&&(tipi_evento_repository.existsByUtenteTeAndTipo(utente, dto.getTipo())) ){
            T_Tipo_evento tipo = tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndTipo(utente, dto.getTipo());
            rispo = attivita_repository. findT_AttivitaByUtenteAttAndTipoEventoAttOrderByPrioritaAsc(utente, tipo);
        } else {
            rispo = attivita_repository.findT_AttivitaByUtenteAttOrderByPrioritaAsc(utente);
        }
        return rispo;
    }

    private List<T_Attivita> postAttivita(DTOAttivita dto, T_Utente utente){
        List<T_Attivita> esito= new ArrayList<>();
        T_Attivita nuova =attivitaService.inserisciAttivita(dto, utente);

        esito.add(nuova);


        return esito;

    }
     private List<T_Attivita>  suggerisciAttivita(T_Utente utente) throws SQLException {
        List<T_Attivita> output= new ArrayList<>();
        Timestamp dal = attivita_repository.inizioPeriodoLibero(utente.getIdUtente());
        Timestamp al = attivita_repository.finePeriodoLibero(utente.getIdUtente(), dal);
        List<Integer> lista = attivita_repository.trovaHint(utente.getIdUtente(), dal, al);
        Integer idAtt = lista.get(0);
        T_Attivita rispo= attivita_repository.getT_AttivitaByUtenteAttAndIdAttivita(utente, idAtt);
        rispo.setCreazione(dal);
        Timestamp fine = eventiService.sommaTempi(dal, rispo.getUmtDurataAtt(),rispo.getDurataAN());
        rispo.setScadenza(fine);
        output.add(rispo);
        return output;
    }
     private List<T_Attivita> pianificaAttivita(DTOAttivita dto, T_Utente utente) throws SQLException{
        List<T_Attivita> output= new ArrayList<>();
        T_Attivita target = trovaAttivitaId(dto, utente);
        Timestamp time = null;
         try {
             time = Timestamp.valueOf(dto.getCreazione().replace('T', ' ')+":00");
         } catch (IllegalArgumentException exception){
             throw new FormatoNonValidoException("formato dell'inizio dell'evento non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
         }
        if(!(target.isPianificata())){
            eventiService.inserisciEventoA(target, time);
            target.setPianificata(true);
        }
        else{
            er.delete(target.getEventi().get(0));
            eventiService.inserisciEventoA(target,time);
        }
        attivita_repository.save(target);
        output.add(target);

        return output;
    }

     public List<T_Attivita> massimizzaAttività(DTOAttivita dto, T_Utente utente) throws SQLException{
        Timestamp dal =null;
        Timestamp al =null;
        Timestamp limiteMaxSlot=null;
        Timestamp limiteMinSlot=null;
        Integer durataSlot= null;
        if(dto.getCreazione()!=null&&dto.getCreazione()!="") {
            dal = Timestamp.valueOf(dto.getCreazione().replace("T", " ") + ":00");
        }else {
            dal = attivita_repository.inizioPeriodoLibero(utente.getIdUtente());
        }
        if(dto.getScadenza()!=null && dto.getScadenza()!=""){
            al= Timestamp.valueOf(dto.getScadenza().replace("T", " ") + ":00");
        }else {
            al = attivita_repository.finePeriodoLibero(utente.getIdUtente(), dal);
        }

        List<SlotTemporale> slot = new ArrayList<>();
        limiteMinSlot=dal;
        while(limiteMinSlot.before(al)){
            limiteMinSlot= attivita_repository.inizioSlot(utente.getIdUtente(),limiteMinSlot, al);
            limiteMaxSlot= attivita_repository.fineSlot(utente.getIdUtente(), limiteMinSlot, al);
            SlotTemporale s =new SlotTemporale();
            s.setInizio(limiteMinSlot);
            s.setFine(limiteMaxSlot);
            durataSlot = attivita_repository.durataSlot(limiteMinSlot, limiteMaxSlot);
            s.setDurataSec(durataSlot);
            slot.add(s);
            limiteMinSlot =limiteMaxSlot;
        }
        List<T_Attivita> candidate = attivita_repository.findT_AttivitaByUtenteAttAndPianificataOrderByDurataSecAsc(utente, false);
        Collections.sort(slot);
        List<T_Attivita> esito = new ArrayList<>();
        Integer iAtt=0;
        Integer iSlot=0;
        Integer nAtt =candidate.size();
        Integer nSlot=slot.size();
        T_Attivita att=null;
        SlotTemporale s=null;
        Integer totsec=0;
        Timestamp partenza =null;
        Timestamp conclusione=null;
         T_UMT umt = umt_repository.getT_UMTByIdUMT("S");
        while ((iSlot<nSlot)&&(iAtt<nAtt)){
            s=slot.get(iSlot);
            att=candidate.get(iAtt);
            if((totsec+att.getDurataSec())<=s.getDurataSec()){
                partenza= eventiService.sommaTempi(s.getInizio(), umt, totsec);
                att.setCreazione(partenza);
                totsec = totsec+att.getDurataSec();
                conclusione=eventiService.sommaTempi(s.getInizio(), umt, totsec);
                att.setScadenza(conclusione);
                esito.add(att);
                iAtt++;
            }else{
                totsec=0;
                iSlot++;
            }

        }
/*
listaSlot order by durataslot
listaAttvita order by duratasec
prima attivita=listattivita.next
while listaSlot.next
   totsec=0
   while attivita.durata+totsec <= durataslot
       leggi T-attivita
       set creazione =inizio
       set scadenza=inizio+duratasec
       risposta.add(Tattivita)
       set inizio=scadenza
       totsec += duratasec
       listaattivita.next

        List<Integer> ids = attivita_repository.trovaRiempitivo(dal, al, utente.getIdUtente());
        List<T_Attivita> rispo = new ArrayList<>();
        Iterator<Integer> i = ids.iterator();

        T_Attivita attivita;
        Timestamp timeDal=dal;
        Timestamp timeAl;
        while (i.hasNext()) {
            attivita =attivita_repository.getT_AttivitaByUtenteAttAndIdAttivita(utente, i.next());

            timeAl=eventiService.sommaTempi(timeDal, umt ,attivita.getDurataSec());
            attivita.setCreazione(timeDal);
            attivita.setScadenza(timeAl);

            rispo.add(attivita);
            timeDal=timeAl;
        }
        return rispo;

 */
         return esito;
    }
    @PostMapping
    public List<T_Attivita> gestioneAttivita(@RequestBody DTOAttivita dto,
                                           @RequestHeader("Authorization") String Token) throws SQLException {

        String username = tokenService.validateTokenAndGetUsername(Token.substring(7));
        T_Utente utente = utenti_repository.getT_UtenteByUserName(username);
        List<T_Attivita> ritorno = null;
        switch (dto.getCRUD()){
            case 'C':
                ritorno = postAttivita(dto,utente);
                break;
            case 'R':
                ritorno = trovaAttivita(dto, utente);
                break;
            case 'U':
                ritorno = modificaAttivita(dto, utente);
                break;
            case 'D':
                ritorno = eliminaAttivita(dto, utente);
                break;
            case 'P':
                ritorno = pianificaAttivita(dto, utente);
                break;
            case 'H':
                ritorno= suggerisciAttivita(utente);
                break;
            case 'M':
                ritorno= massimizzaAttività(dto, utente);
                break;
        }
        return ritorno;
    }
    private List<T_Attivita> modificaAttivita(DTOAttivita dto, T_Utente utente){
        List<T_Attivita> risposta = new ArrayList<>();
        T_Attivita target= trovaAttivitaId(dto, utente);

        risposta.add(attivitaService.aggiornaAttivita(target, dto));

        List<T_Evento> condannati= er.findT_EventoByUtenteEvAndAttivita(utente, target);
        Iterator<T_Evento> i= condannati.iterator();
        while(i.hasNext()){
            er.delete(i.next());
        }

        return risposta;
    }
    private List<T_Attivita> eliminaAttivita(DTOAttivita dto, T_Utente utente){
        List<T_Attivita> risposta = new ArrayList<>();
        T_Attivita target= trovaAttivitaId(dto, utente);
        attivita_repository.delete(target);

        return risposta;
    }

}
