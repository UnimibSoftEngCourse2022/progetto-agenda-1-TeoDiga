package com.example.demo.service;

import com.example.demo.DTO.DTOImpegno;
import com.example.demo.eccezioni.*;
import com.example.demo.model.T_Tipo_evento;
import com.example.demo.model.T_Impegno;
import com.example.demo.model.T_UMT;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Impegni_repository;
import com.example.demo.repository.Tipi_evento_repository;
import com.example.demo.repository.UMT_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;

@Service
public class ImpegniService {
    @Autowired
    private Impegni_repository impegni_repository;
    @Autowired
    private UMT_repository umt_repository;
    @Autowired
    private  Tipi_evento_repository tipi_evento_repository;
    public T_Impegno inserisciImpegno(DTOImpegno dto, T_Utente utente){
        T_Impegno impegno = new T_Impegno();
        impegno.setTitolo(dto.getTitolo());

        impegno.setDescrizione(dto.getDescrizione());
        impegno.setUtenteImp(utente);
        if(tipi_evento_repository.existsByUtenteTeAndTipo(utente, dto.getTipo())){
            T_Tipo_evento te= tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndTipo(utente, dto.getTipo());
            impegno.setTipoEventoImp(te);
        }
        else{
            throw new NonTrovatoException("non esiste il tipo di attività "+dto.getTipo());
        }
        T_UMT freq, durata, all;
        if((dto.getFreqN()!=null)&&(dto.getFreqN()>0)){
            if((dto.getFreqN()>0)&&((dto.getFreq()==null))){
                throw new FormatoNonValidoException("formato frequenza non valido");
            }
            else{
                impegno.setFrequenzaIN(dto.getFreqN());
                freq = umt_repository.getT_UMTByIdUMT(dto.getFreq());
                impegno.setUmtFreqImp(freq);
            }
        }
        if((dto.getDurataN()!=null)&&(dto.getDurataN()>0)){
            if((dto.getDurataN()<=0)&&((dto.getDurata()==null))){
                throw new FormatoNonValidoException("formato durata non valido");
            }
            else{
                impegno.setDurataIN(dto.getDurataN());
                durata= umt_repository.getT_UMTByIdUMT(dto.getDurata());
                impegno.setUmtDurataImp(durata);

            }
        }
        if((dto.getAllN()!=null)&&(dto.getAllN()>0)){
            if((dto.getAllN()>0)&&((dto.getAll()==null))){
                throw new FormatoNonValidoException("formato allarme non valido");
            }
            else{
                impegno.setAllarmeIN(dto.getAllN());
                all = umt_repository.getT_UMTByIdUMT(dto.getAll());
                impegno.setUmtAllImp(all);
            }
        }


            try {
                impegno.setInizioImpegno(Timestamp.valueOf(dto.getInizioImpegno().replace('T', ' ') + ":00"));
            } catch (IllegalArgumentException exception) {
                throw new FormatoNonValidoException("formato dell'inizio dell'impegno non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }


            try {
                impegno.setFineImpegno(Timestamp.valueOf(dto.getFineImpegno().replace('T', ' ') + ":00"));
            } catch (IllegalArgumentException exception) {
                throw new FormatoNonValidoException("formato della fine dell'impegno non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }


        return  impegni_repository.save(impegno);
    }
    public T_Impegno aggiornaImpegno(T_Impegno impegno, DTOImpegno dto) {
        T_Utente utente = impegno.getUtenteImp();
        if(dto.getTitolo()!= null){
            impegno.setTitolo(dto.getTitolo());
        }
        if(dto.getDescrizione()!= null){
            impegno.setDescrizione(dto.getDescrizione());
        }
        if(dto.getTipo()!= null){
            if(tipi_evento_repository.existsByUtenteTeAndTipo(utente, dto.getTipo())){
                T_Tipo_evento te= tipi_evento_repository.getT_Tipo_eventoByUtenteTeAndTipo(utente, dto.getTipo());
                impegno.setTipoEventoImp(te);
            }
            else{
                throw new NonTrovatoException("non esiste il tipo di impegno "+dto.getTipo());
            }
        }
        T_UMT freq, durata, all;
        if((dto.getFreqN()!= null)&&(dto.getFreqN()>=0)){
            if((dto.getFreqN()>0)&&(dto.getFreq()==null)){
                throw new FormatoNonValidoException("formato della frequenza non valido");
            }
            else if((dto.getFreqN()==0)&&((dto.getFreq()==null))){
                impegno.setFrequenzaIN(0);
                impegno.setUmtFreqImp(null);
            }
            else {
                impegno.setFrequenzaIN(dto.getFreqN());
                freq = umt_repository.getT_UMTByIdUMT(dto.getFreq());
                impegno.setUmtFreqImp(freq);
            }
        }
        if((dto.getDurataN()!= null)&&(dto.getDurataN()>=0)){
            if((dto.getDurataN()>0)&&(dto.getDurata()==null)){
                throw new FormatoNonValidoException("formato della durata non valido");
            }
            else if((dto.getDurataN()==0)&&((dto.getDurata()==null))){
                throw new FormatoNonValidoException("non si può avere durata nulla");
            }
            else {
                impegno.setDurataIN(dto.getDurataN());
                durata = umt_repository.getT_UMTByIdUMT(dto.getDurata());
                impegno.setUmtDurataImp(durata);
            }
        }
        if((dto.getAllN()!= null)&&(dto.getAllN()>=0)){
            if((dto.getAllN()>0)&&(dto.getAll()==null)){
                throw new FormatoNonValidoException("formato dell'allarme non valido");
            }
            else if((dto.getFreqN()==0)&&((dto.getFreq()==null))){
                impegno.setFrequenzaIN(0);
                impegno.setUmtFreqImp(null);
            }
            else {
                impegno.setFrequenzaIN(dto.getFreqN());
                freq = umt_repository.getT_UMTByIdUMT(dto.getFreq());
                impegno.setUmtFreqImp(freq);
            }
        }
        Timestamp inizio= null;
        Timestamp fine= null;

        if(dto.getInizioImpegno()!=null){
            try {
                inizio= Timestamp.valueOf(dto.getInizioImpegno().replace('T', ' ')+":00");
            } catch (IllegalArgumentException exception){
                throw new FormatoNonValidoException("formato dell'inizio dell'impegno non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }
            if(impegno.getInizioImpegno()!=inizio){
                impegno.setInizioImpegno(inizio);
            }
        }
        if(dto.getFineImpegno()!=null){
            try {
                fine = Timestamp.valueOf(dto.getFineImpegno().replace('T', ' ')+":00");
            } catch (IllegalArgumentException exception){
                throw new FormatoNonValidoException("formato della fine dell'impegno non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
            }
            if(impegno.getFineImpegno()!=fine){
                impegno.setFineImpegno(fine);
            }
        }

        return impegni_repository.save(impegno);
    }
}
