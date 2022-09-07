package com.example.demo.service;

import com.example.demo.DTO.DTOAttivita;
import com.example.demo.model.T_Attivita;
import com.example.demo.model.T_Impegno;
import com.example.demo.repository.Utenti_repository;
import com.example.demo.model.T_Tipo_evento;
import com.example.demo.model.T_UMT;
import com.example.demo.eccezioni.*;
import com.example.demo.repository.UMT_repository;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Attivita_repository;
import com.example.demo.repository.Tipi_evento_repository;
import com.example.demo.repository.Eventi_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
public class AttivitaService {
    @Autowired
    private Tipi_evento_repository ter;
    @Autowired
    private UMT_repository umt_repository;
    @Autowired
    private Attivita_repository attivita_repository;
    @Autowired
    private Eventi_repository eventi_repository;
    @Transactional
    public T_Attivita inserisciAttivita(DTOAttivita dto, T_Utente utente){
        T_Attivita attivita = new T_Attivita();
        attivita.setTitolo(dto.getTitolo());
        T_UMT durata, all;
        attivita.setPianificata(false);
        attivita.setDescrizione(dto.getDescrizione());
        attivita.setUtenteAtt(utente);
        if(ter.existsByUtenteTeAndTipo(utente, dto.getTipo())){
            T_Tipo_evento te= ter.getT_Tipo_eventoByUtenteTeAndTipo(utente, dto.getTipo());
            attivita.setTipoEventoAtt(te);
        }
        else{
            throw new NonTrovatoException("non esiste il tipo di attività "+dto.getTipo());
        }
        if((dto.getDurataN()!=null)&&(dto.getDurataN()>0)){
            if((dto.getDurataN()>0)&&((dto.getDurata()==null))){
                throw new FormatoNonValidoException("formato durata non valido");
            }
            else{
                attivita.setDurataAN(dto.getDurataN());
                durata = umt_repository.getT_UMTByIdUMT(dto.getDurata());
                attivita.setUmtDurataAtt(durata);
            }
        }
        if((dto.getAllarmeN()!=null)&&(dto.getAllarmeN()>0)){
            if((dto.getAllarmeN()>0)&&((dto.getAllarme()==null))){
                throw new FormatoNonValidoException("formato allarme non valido");
            }
            else{
                attivita.setAllarmeAN(dto.getDurataN());
                all = umt_repository.getT_UMTByIdUMT(dto.getAllarme());
                attivita.setUmtAllAtt(all);
            }
        }

        try {
            attivita.setCreazione(Timestamp.valueOf(dto.getCreazione().replace('T', ' ')+":00"));
        } catch (IllegalArgumentException exception){
            throw new FormatoNonValidoException("formato dell'inizio dell'attivita non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
        }
        try {
            attivita.setScadenza(Timestamp.valueOf(dto.getScadenza().replace('T', ' ')+":00"));
        } catch (IllegalArgumentException exception){
            throw new FormatoNonValidoException("formato della scadenza attivita non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
        }
        T_Attivita rispo=null;
        rispo =  attivita_repository.save(attivita);

        return rispo;
    }
    public T_Attivita aggiornaAttivita(T_Attivita attivita, DTOAttivita dto){
        T_Utente utente= attivita.getUtenteAtt();
        if(dto.getTitolo()!=null){
            attivita.setTitolo(dto.getTitolo());
        }
        if(dto.getDescrizione()!=null){
            attivita.setDescrizione(dto.getDescrizione());
        }
        if(dto.getTipo()!=null){
            if(ter.existsByUtenteTeAndTipo(utente, dto.getTipo())){
                T_Tipo_evento te= ter.getT_Tipo_eventoByUtenteTeAndTipo(utente, dto.getTipo());
                attivita.setTipoEventoAtt(te);
            }
            else{
                throw new NonTrovatoException("non esiste il tipo di attività "+dto.getTipo());
            }
        }
        T_UMT durata, all;
        if((dto.getDurataN()!= null)&&(dto.getDurataN()>=0)){
            if((dto.getDurataN()>0)&&(dto.getDurata()==null)){
                throw new FormatoNonValidoException("formato della durata non valido");
            }
            else if((dto.getDurataN()==0)&&((dto.getDurata()==null))){
                throw new FormatoNonValidoException("non si può avere durata nulla");
            }
            else {
                attivita.setDurataAN(dto.getDurataN());
                durata = umt_repository.getT_UMTByIdUMT(dto.getDurata());
                attivita.setUmtDurataAtt(durata);
            }
        }
        if((dto.getAllarme()!= null)&&(dto.getAllarmeN()>=0)){
            if((dto.getAllarmeN()>0)&&(dto.getAllarme()==null)){
                throw new FormatoNonValidoException("formato dell'allarme non valido");
            }
            else if((dto.getAllarmeN()==0)&&((dto.getAllarme()==null))){
                attivita.setAllarmeAN(0);
                attivita.setUmtAllAtt(null);
            }
            else {
                attivita.setAllarmeAN(dto.getAllarmeN());
                all = umt_repository.getT_UMTByIdUMT(dto.getAllarme());
                attivita.setUmtAllAtt(all);
            }
        }
        Timestamp creaz= null;
        Timestamp scad= null;
        if(dto.getCreazione()!=null){
            try {
                creaz =Timestamp.valueOf(dto.getCreazione().replace('T', ' ')+":00");

            } catch (IllegalArgumentException exception){
                throw new FormatoNonValidoException("formato dell'inizio dell'impegno non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }
            if(attivita.getCreazione()!=creaz){
                attivita.setCreazione(creaz);
                if(attivita.isPianificata()){
                    eventi_repository.delete(attivita.getEventi().get(0));
                    attivita.setPianificata(false);
                }
            }
        }
        if(dto.getScadenza()!=null){
            try {
                scad = Timestamp.valueOf(dto.getScadenza().replace('T', ' ')+":00");

            } catch (IllegalArgumentException exception){
                throw new FormatoNonValidoException("formato della fine dell'impegno non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }
            if (attivita.getScadenza()!=scad){
                attivita.setScadenza(scad);
                if(attivita.isPianificata()){
                    eventi_repository.delete(attivita.getEventi().get(0));
                    attivita.setPianificata(false);
                }
            }

        }
        return attivita_repository.save(attivita);
    }
}
