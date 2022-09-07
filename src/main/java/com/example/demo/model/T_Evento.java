package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "EVENTI", schema = "APP")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class T_Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    public Integer idEvento;
    @Column(name = "orario_inizio")
    public Timestamp orarioInizio;
    @Column(name = "orario_fine")
    public Timestamp orarioFine;
    @Column(name = "orario_allarme")
    public Timestamp orarioAllarme;
    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnore
    private T_Utente utenteEv;


    @ManyToOne
    @JoinColumn(name = "id_impegno")

    private T_Impegno impegno;

    @ManyToOne
    @JoinColumn(name = "id_attivita")

    private T_Attivita attivita;

}
