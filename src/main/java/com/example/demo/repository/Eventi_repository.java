package com.example.demo.repository;

import com.example.demo.model.T_Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

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
}
