package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface Eventi_repository extends JpaRepository<T_Evento, Integer> {

    @Query(value = "select {fn timestampadd(SQL_TSI_YEAR,?1, ?2)} from sysibm.sysdummy1", nativeQuery = true)
    Timestamp sommaAnni(Integer num, Timestamp inizio);
    @Query(value = "select {fn timestampadd(SQL_TSI_MONTH,?1, ?2)} from sysibm.sysdummy1", nativeQuery = true)
    Timestamp sommaMesi(Integer num, Timestamp inizio);
    @Query(value = "select {fn timestampadd(SQL_TSI_DAY,?1, ?2)} from sysibm.sysdummy1", nativeQuery = true)
    Timestamp sommaGiorni(Integer num, Timestamp inizio);
    @Query(value = "select {fn timestampadd(SQL_TSI_HOUR,?1, ?2)} from sysibm.sysdummy1", nativeQuery = true)
    Timestamp sommaOre(Integer num, Timestamp inizio);
    @Query(value = "select {fn timestampadd(SQL_TSI_MINUTE,?1, ?2)} from sysibm.sysdummy1", nativeQuery = true)
    Timestamp sommaMinuti(Integer num, Timestamp inizio);
    @Query(value = "select {fn timestampadd(SQL_TSI_SECOND,?1, ?2)} from sysibm.sysdummy1", nativeQuery = true)
    Timestamp sommaSecondi(Integer num, Timestamp inizio);
    @Query(value = "select {fn timestampdiff(SQL_TSI_SECOND, ?1, ?2)} from sysibm.sysdummy1", nativeQuery = true)
    Integer sottraiTimestamp(Timestamp t1, Timestamp t2);
    @Query(value = "select e.id_evento from APP.EVENTI e " +
            "left outer join APP.IMPEGNI i on e.id_impegno =i.id_impegno "+
            "left outer join APP.ATTIVITA a on e.id_attivita =a.id_attivita "+
            "where a.id_tipo=?2 or i.id_tipo=?2 and e.id_utente=?1 "+
            "order by e.orario_inizio", nativeQuery = true)
    List<Integer> trovaEventiPerTipo(Integer idUtente, Integer idTipo);
    @Query(value = "select e.id_evento from APP.EVENTI e "+
            "left outer join APP.IMPEGNI i on e.id_impegno=i.id_impegno "+
            "left outer join APP.ATTIVITA a on e.id_attivita=a.id_attivita "+
            "where value(a.titolo, i.titolo) Like ?2 and e.id_utente=?1 "+
            "order by e.orario_inizio", nativeQuery = true)
    List<Integer> trovaEventiPerTitolo(Integer idUtente, String titolo);

    @Query(value = "select e.id_evento from APP.EVENTI e " +
            "left outer join APP.IMPEGNI i on e.id_impegno =i.id_impegno "+
            "left outer join APP.ATTIVITA a on e.id_attivita =a.id_attivita "+
            "WHERE (a.titolo like ?1 or i.titolo like ?1)" +
            " AND (a.id_tipo BETWEEN ?2 and ?3 OR i.id_tipo BETWEEN ?2 and ?3) " +
            " AND  e.ORARIO_INIZIO <= ?5 and e.Orario_fine>=?4 " +
            " AND e.id_utente=?6 order by e.orario_inizio", nativeQuery = true)
    List<Integer> filtraEventi(String titolo, Integer idTipoMin,Integer idTipoMax, Timestamp dal, Timestamp Al, Integer idUtente);
    List<T_Evento> findT_EventoByUtenteEvOrderByOrarioInizioAsc(T_Utente utente);
    List<T_Evento> findT_EventoByUtenteEvAndImpegnoOrderByOrarioInizioAsc(T_Utente utente, T_Impegno impegno);
    List<T_Evento>findT_EventoByUtenteEvAndAttivita(T_Utente utente, T_Attivita attivita);
    boolean existsT_EventoByUtenteEvAndIdEvento(T_Utente utente, Integer id);
    T_Evento getT_EventoByUtenteEvAndIdEvento(T_Utente utente, Integer id);


}
