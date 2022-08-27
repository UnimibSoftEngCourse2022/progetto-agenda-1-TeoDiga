package com.example.demo.repository;

import com.example.demo.model.T_Tipo_contatto;
import com.example.demo.model.T_Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tipi_contatto_repository extends JpaRepository<T_Tipo_contatto, Integer> {
    List<T_Tipo_contatto> findT_Tipo_contattoByIdTipoContatto(Integer id);
    T_Tipo_contatto getT_Tipo_contattoByIdTipoContatto(Integer id);

    List<T_Tipo_contatto> findT_Tipo_contattoByUtenteTc(T_Utente utente);
    List<T_Tipo_contatto> findT_Tipo_contattoByUtenteTcAndTipo(T_Utente utente, String tipo);
    boolean existsT_Tipo_contattoByUtenteTcAndTipo(T_Utente utente, String tipo);
    boolean existsT_Tipo_contattoByUtenteTcAndIdTipoContatto(T_Utente utente, Integer id);
    T_Tipo_contatto getT_Tipo_contattoByUtenteTcAndTipo(T_Utente utente, String tipo);
    List<T_Tipo_contatto> findT_Tipo_contattoByUtenteTcAndIdTipoContatto(T_Utente utente, Integer id);
    T_Tipo_contatto getT_Tipo_contattoByUtenteTcAndIdTipoContatto(T_Utente utente, Integer id);
}
