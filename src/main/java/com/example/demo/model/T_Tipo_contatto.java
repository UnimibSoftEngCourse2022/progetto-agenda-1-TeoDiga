package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TIPI_CONTATTO", schema = "APP")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class T_Tipo_contatto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_contatto")
    public Integer idTipoContatto;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnore
    private T_Utente utenteTc;


    public String tipo, descrizione, colore;

    @OneToMany(mappedBy = "tipoContattoCon")
    @JsonBackReference
    private List<T_Contatto> contatti;
}
