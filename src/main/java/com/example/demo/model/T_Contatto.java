package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CONTATTI", schema = "APP")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class T_Contatto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contatto")
    public Integer idContatto;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnore
    private T_Utente utenteCon;
    @ManyToOne
    @JoinColumn(name = "id_tipo_contatto")
    @JsonProperty("tipoContattoCon")
    @JsonIgnore
    private T_Tipo_contatto tipoContattoCon;

    public String email, telefono;
    @Column(name = "nome_cognome")
    String nomeCognome;
}
