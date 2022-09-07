package com.example.demo.repository;

import com.example.demo.model.T_Impegno;
import com.example.demo.model.T_Tipo_evento;
import com.example.demo.model.T_Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Impegni_repository extends JpaRepository<T_Impegno, Integer> {
    List<T_Impegno> findT_ImpegnoByUtenteImp(T_Utente utente);
    List<T_Impegno> findT_ImpegnoByUtenteImpAndTipoEventoImp(T_Utente utente, T_Tipo_evento tipo);
    T_Impegno getT_ImpegnoByUtenteImpAndTitolo(T_Utente utente, String nome);
    boolean existsT_ImpegnoByUtenteImpAndIdImpegno(T_Utente utente, Integer id);
    T_Impegno getT_ImpegnoByUtenteImpAndIdImpegno(T_Utente utente, Integer id);
    boolean existsT_ImpegnoByUtenteImpAndTitolo(T_Utente utente, String s);

    List<T_Impegno> findT_ImpegnoByUtenteImpAndTitolo(T_Utente utente, String s);
}
