package com.example.demo.service;

import com.example.demo.eccezioni.FormatoNonValidoException;
import com.example.demo.model.*;
import com.example.demo.repository.Eventi_repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.T_UMT;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.example.demo.repository.UMT_repository;
import com.example.demo.DTO.DTOEvento;




@Service
public class EventiService {
    @Autowired
    Eventi_repository eventi_repository;
    @Autowired
    private UMT_repository umt_repository;
    public void inserisciEventoI(T_Impegno impegno, Timestamp dataOccorenza) throws SQLException {
        T_Evento ev = new T_Evento();
        ev.setUtenteEv(impegno.getUtenteImp());
        ev.setImpegno(impegno);
        ev.setOrarioInizio(dataOccorenza);

        Timestamp fine = sommaTempi(dataOccorenza, impegno.getUmtDurataImp(), impegno.getDurataIN());
        Timestamp allarme = sottraiTempi(dataOccorenza, impegno.getUmtAllImp(), impegno.getAllarmeIN());
        ev.setOrarioFine(fine);
        ev.setOrarioAllarme(allarme);
        if(impegno.getFrequenzaIN()==0){
            eventi_repository.save(ev);
        }
        else if(fine.before(impegno.getFineImpegno())){
            eventi_repository.save(ev);

            Timestamp nuovaData = sommaTempi(dataOccorenza, impegno.getUmtFreqImp(), impegno.getFrequenzaIN());
            inserisciEventoI(impegno, nuovaData);
        }
    }
    public void inserisciEventoA(T_Attivita attivita, Timestamp data) throws SQLException{
        T_Evento ev = new T_Evento();
        ev.setUtenteEv(attivita.getUtenteAtt());
        ev.setAttivita(attivita);
        ev.setOrarioInizio(data);
        Timestamp fine = sommaTempi(data, attivita.getUmtDurataAtt(), attivita.getDurataAN());
        Timestamp allarme = sottraiTempi(data, attivita.getUmtAllAtt(), attivita.getAllarmeAN());
        ev.setOrarioFine(fine);
        ev.setOrarioAllarme(allarme);
        eventi_repository.save(ev);
    }
    public Timestamp sommaTempi(Timestamp inizio, T_UMT umt, Integer num) throws SQLException{

        Timestamp output = null;

        switch (umt.getIdUMT()){
            case "A":
                output = eventi_repository.sommaAnni(num, inizio);
                break;
            case "M":
                output = eventi_repository.sommaMesi(num, inizio);
                break;
            case "G":
                output = eventi_repository.sommaGiorni(num, inizio);
                break;
            case "O":
                output = eventi_repository.sommaOre(num, inizio);
                break;
            case "N":
                output = eventi_repository.sommaMinuti(num, inizio);
                break;
            case "S":
                output = eventi_repository.sommaSecondi(num, inizio);
                break;
        }

        return output;

    }
    private  Timestamp sottraiTempi(Timestamp input, T_UMT umt, Integer num) throws SQLException{
        Integer num2 = num*(-1);
        return sommaTempi(input, umt, num2);
    }
    public T_Evento aggiornaEvento(T_Evento evento, DTOEvento dto) throws SQLException{
        Timestamp inizio= null;
        Timestamp fine= null;
        Timestamp sveglia =null;
        Integer differenza = null;
        T_UMT umt= umt_repository.getT_UMTByIdUMT("S");
        if(dto.getInizio()!=null) {
            try {
                inizio = Timestamp.valueOf(dto.getInizio().replace('T', ' ') + ":00");
                differenza = eventi_repository.sottraiTimestamp(inizio, evento.getOrarioInizio());
            } catch (IllegalArgumentException exception) {
                throw new FormatoNonValidoException("formato dell'inizio dell'evento non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }
        }
        if(dto.getFine()!=null){
            try {
                fine =Timestamp.valueOf(dto.getFine().replace('T', ' ')+":00");

            } catch (IllegalArgumentException exception){
                throw new FormatoNonValidoException("formato della fine dell'evento non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }
        }
        if(dto.getSveglia()!=null){
            try {
                sveglia =Timestamp.valueOf(dto.getSveglia().replace('T', ' ')+":00");

            } catch (IllegalArgumentException exception){
                throw new FormatoNonValidoException("formato della sveglia non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }
        }
        if(evento.getOrarioInizio()!=inizio){
            evento.setOrarioInizio(inizio);
            if (evento.getOrarioFine()!= fine){
                evento.setOrarioFine(fine);
            }else{
                fine= sommaTempi(evento.getOrarioFine(), umt, differenza);
                evento.setOrarioFine(fine);
            }
            if(evento.getOrarioAllarme()!=sveglia){
                evento.setOrarioAllarme(sveglia);
            }else{
                sveglia=sommaTempi(evento.getOrarioAllarme(), umt, differenza);
                evento.setOrarioAllarme(sveglia);
            }
        }else{
            if (evento.getOrarioFine()!= fine){
                evento.setOrarioFine(fine);
            }
            if(evento.getOrarioAllarme()!=sveglia){
                evento.setOrarioAllarme(sveglia);
            }
        }

        return eventi_repository.save(evento);
    }
}
