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
    public Integer id_evento;
    public Timestamp orario_inizio, orario_fine, orario_allarme;
    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnore
    private T_Utente utenteEv;


    @ManyToOne
    @JoinColumn(name = "id_impegno")
    @JsonIgnore
    private T_Impegno impegno;

    @ManyToOne
    @JoinColumn(name = "id_attivita")
    @JsonIgnore
    private T_Attivita attivita;

}
