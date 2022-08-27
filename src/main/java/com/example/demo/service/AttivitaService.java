package com.example.demo.service;

import com.example.demo.DTO.DTOAttivita;
import com.example.demo.model.T_Attivita;
import com.example.demo.model.T_Impegno;
import com.example.demo.model.T_UMT;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Attivita_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
public class AttivitaService {
    @Autowired
    private Attivita_repository attivita_repository;
    @Transactional
    public T_Attivita inserisciAttivita(DTOAttivita dto, T_Utente utente, T_UMT durata, T_UMT all, Timestamp prova){
        T_Attivita attivita = new T_Attivita();
        attivita.setTitolo(dto.getTitolo());

        attivita.setDescrizione(dto.getDescrizione());
        attivita.setUtenteAtt(utente);


        attivita.setUmtDurataAtt(durata);
        attivita.setDurataAN(dto.getDurataN());
        attivita.setUmtAllAtt(all);
        attivita.setAllarmeAN(dto.getAllarmeN());

        attivita.setCreazione(prova);
        attivita.setScadenza(Timestamp.valueOf(dto.getScadenza()));

        T_Attivita rispo=null;
        rispo =  attivita_repository.save(attivita);
        attivita_repository.setPriorita(utente.getIdUtente());
        return rispo;
    }

}
