package com.example.demo.service;

import com.example.demo.model.T_Evento;
import com.example.demo.model.T_Impegno;
import com.example.demo.model.T_UMT;
import com.example.demo.model.T_Utente;
import com.example.demo.repository.Eventi_repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;



@Service
public class EventiService {
    @Autowired
    Eventi_repository eventi_repository;
    public void inserisciEvento(T_Impegno impegno, Timestamp dataOccorenza) throws SQLException {
        T_Evento ev = new T_Evento();
        ev.setUtenteEv(impegno.getUtenteImp());
        ev.setImpegno(impegno);
        ev.setOrario_inizio(dataOccorenza);

        Timestamp fine = sommaTempi(dataOccorenza, impegno.getUmtDurataImp(), impegno.getDurataIN());
        Timestamp allarme = sottraiTempi(dataOccorenza, impegno.getUmtAllImp(), impegno.getAllarmeIN());
        ev.setOrario_fine(fine);
        ev.setOrario_allarme(allarme);
        if(fine.before(impegno.getFine_impegno())) {
            eventi_repository.save(ev);

            Timestamp nuovaData = sommaTempi(dataOccorenza, impegno.getUmtFreqImp(), impegno.getFrequenzaIN());
            inserisciEvento(impegno, nuovaData);
        }
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

}
