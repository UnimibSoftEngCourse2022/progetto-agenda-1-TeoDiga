package com.example.demo.repository;

import com.example.demo.model.T_Attivita;
import com.example.demo.model.T_Tipo_evento;
import com.example.demo.model.T_Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface Attivita_repository extends JpaRepository<T_Attivita, Integer> {
    boolean existsT_AttivitaByUtenteAttAndIdAttivita(T_Utente utente, Integer id);
    boolean existsT_AttivitaByUtenteAttAndTitolo(T_Utente utente, String s);
    List<T_Attivita> findT_AttivitaByUtenteAttAndPianificata(T_Utente utente, boolean b);
    List<T_Attivita> findT_AttivitaByUtenteAttAndPianificataOrderByDurataSecAsc(T_Utente utente, boolean b);
    List<T_Attivita> findT_AttivitaByUtenteAttAndTitolo(T_Utente utente, String nome);
    List<T_Attivita> findT_AttivitaByUtenteAttOrderByPrioritaAsc(T_Utente utente);
    List<T_Attivita> findT_AttivitaByUtenteAttAndTipoEventoAttOrderByPrioritaAsc(T_Utente utente, T_Tipo_evento tipo);
    T_Attivita getT_AttivitaByUtenteAttAndTitolo(T_Utente utente, String titolo);
    T_Attivita getT_AttivitaByUtenteAttAndIdAttivita(T_Utente utente, Integer id);
    @Modifying
    @Query(value = "update APP.ATTIVITA a set a.priorita = " +
            "case a.pianificata when true then null when false then (select count (*) from APP.ATTIVITA " +
            "where id_utente = a.id_utente and pianificata=false and scadenza < a.scadenza) end " +
            "where a.id_utente=?1", nativeQuery = true)
    void setPriorita(Integer id_utente);

    @Query(value = "select value( max(orario_fine), {fn timestampadd(SQL_TSI_HOUR,2,current_timestamp)}) from APP.EVENTI " +
            "where id_utente=?1 and orario_inizio <= {fn timestampadd(SQL_TSI_HOUR,2,current_timestamp)} " +
            "and orario_fine > {fn timestampadd(SQL_TSI_HOUR,2,current_timestamp)}", nativeQuery = true)
    Timestamp inizioPeriodoLibero(Integer id_utente);
    @Query(value = "select value(max(orario_fine), ?2) from APP.EVENTI "+
            "where id_utente=?1 and orario_inizio <=?2 and orario_fine > ?2 and orario_fine < ?3", nativeQuery = true)
    Timestamp inizioSlot(Integer id_utente, Timestamp partenza, Timestamp limite);
    @Query(value =  "select min (x.orario_inizio) as orario_inizio from "+
            "(select orario_inizio from APP.EVENTI "+
            " where id_utente=?1 and orario_inizio> ?2" +
            " union " +
            " select value(?3,current_timestamp) as orario_inizio from sysibm.sysdummy1) x", nativeQuery = true)
    Timestamp fineSlot(Integer id_utente, Timestamp partenza, Timestamp limite);
    @Query(value = "select {fn timestampdiff(SQL_TSI_SECOND, ?1, ?2)} from sysibm.sysdummy1", nativeQuery = true)
    Integer durataSlot( Timestamp dal, Timestamp al);
    @Query(value ="select value(min(orario_inizio), {fn timestampadd(SQL_TSI_YEAR, 100, ?2)}) from APP.EVENTI "+
            "where id_utente=?1 and orario_inizio >= ?2", nativeQuery = true)
    Timestamp finePeriodoLibero(Integer id_utente, Timestamp inizioPeriodo);

    @Query(value = "select id_attivita from APP.ATTIVITA " +
            "where id_utente = ?1 and pianificata=false and creazione<=?2  " +
            "and case durata_a_um  " +
            "when 'A' then {fn timestampadd(SQL_TSI_YEAR, durata_a_n, ?2)} " +
            "when 'M' then {fn timestampadd(SQL_TSI_MONTH, durata_a_n, ?2)} " +
            "when 'G' then {fn timestampadd(SQL_TSI_DAY, durata_a_n, ?2)} " +
            "when 'O' then {fn timestampadd(SQL_TSI_HOUR, durata_a_n, ?2)} " +
            "when 'N' then {fn timestampadd(SQL_TSI_MINUTE, durata_a_n, ?2)} " +
            "when 'S' then {fn timestampadd(SQL_TSI_SECOND, durata_a_n, ?2)} " +
            "end <=?3 " +
            "order by priorita, creazione, id_attivita", nativeQuery = true)
    List<Integer> trovaHint(Integer id_utente, Timestamp inizio, Timestamp fine);
    @Query(value = "select x.id_attivita from " +
            "(select id_attivita, durata_sec, pianificata, " +
            "(select sum (i.durata_sec) from APP.ATTIVITA i where i.id_utente=a.id_utente and i.pianificata=a.pianificata " +
            "    and (i.durata_sec<a.durata_Sec or (i.durata_Sec=a.durata_sec and i.id_attivita<=a.id_attivita))) as prog_sec " +
            "from APP.ATTIVITA a " +
            "where a.id_utente=?3 and a.pianificata=false " +
            "order by a.durata_sec, a.id_attivita) as x " +
            "where prog_sec <= {fn timestampdiff(SQL_TSI_SECOND, ?1, ?2)}", nativeQuery = true)
    List<Integer> trovaRiempitivo(Timestamp inizio, Timestamp fine, Integer id_utente);

}
