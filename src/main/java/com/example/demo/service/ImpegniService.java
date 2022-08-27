package com.example.demo.service;

import com.example.demo.DTO.DTOImpegno;
import com.example.demo.eccezioni.FormatoNonValidoException;
import com.example.demo.model.T_Impegno;
import com.example.demo.model.T_UMT;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Impegni_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;

@Service
public class ImpegniService {
    @Autowired
    Impegni_repository impegni_repository;


    public T_Impegno inserisciImpegno(DTOImpegno dto, T_Utente utente, T_UMT freq, T_UMT durata, T_UMT all ){
        T_Impegno impegno = new T_Impegno();
        impegno.setTitolo(dto.getTitolo());

        impegno.setDescrizione(dto.getDescrizione());
        impegno.setUtenteImp(utente);

        impegno.setUmtFreqImp(freq);
        impegno.setFrequenzaIN(dto.getFreqN());
        impegno.setUmtDurataImp(durata);
        impegno.setDurataIN(dto.getDurataN());
        impegno.setUmtAllImp(all);
        impegno.setAllarmeIN(dto.getAllN());

        try {
          impegno.setInizio_impegno(Timestamp.valueOf(dto.getInizioImpegno()));
        } catch (IllegalArgumentException exception){
            throw new FormatoNonValidoException("formato dell'inizio dell'impegno non è un timestamp valido\nusare: YYYY-MM-DD HH:MM:SS");
        }

        impegno.setFine_impegno(Timestamp.valueOf(dto.getFineImpegno()));

        return  impegni_repository.save(impegno);
    }
    public T_Impegno aggiornaImpegno() {
        T_Impegno rispo=null;
        return rispo;
    }
}
