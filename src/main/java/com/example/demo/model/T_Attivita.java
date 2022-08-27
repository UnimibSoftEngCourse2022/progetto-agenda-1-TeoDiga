package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "ATTIVITA", schema = "APP")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class T_Attivita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_attivita")
    public Integer idAttivita;

    public String titolo, descrizione;
    public Timestamp creazione, scadenza;
    public boolean pianificata;
    public Integer priorita;
    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnore
    private T_Utente utenteAtt;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    @JsonIgnore
    private T_Tipo_evento tipoEventoAtt;

    @ManyToOne
    @JoinColumn(name = "durata_a_um")
    @JsonIgnore
    private T_UMT umtDurataAtt;

    @ManyToOne
    @JoinColumn(name = "allarme_a_um")
    @JsonIgnore
    private T_UMT umtAllAtt;

    @Column(name = "durata_a_n")
    public Integer durataAN;
    @Column(name = "durata_sec")
    public Integer durataSec;
    @Column(name = "allarme_a_n")
    public Integer allarmeAN;
    @OneToMany(mappedBy = "attivita")
    private List<T_Evento> eventi;
}
