package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name ="IMPEGNI", schema = "APP")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class T_Impegno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_impegno")
    public Integer idImpegno;
    public String titolo, descrizione;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnore
    private T_Utente utenteImp;
    @ManyToOne
    @JoinColumn(name = "id_tipo")

    private T_Tipo_evento tipoEventoImp;
    @Column(name = "inizio_impegno")
    public Timestamp inizioImpegno;
    @Column(name = "fine_impegno")
    public Timestamp fineImpegno;

    @Column(name ="frequenza_i_n")
    private Integer frequenzaIN;
    @Column(name = "durata_i_n" )
    private Integer durataIN;
    @Column(name = "allarme_n")
    private Integer allarmeIN;

    @ManyToOne
    @JoinColumn(name = "frequenza_i_um")

    private T_UMT umtFreqImp;

    @ManyToOne
    @JoinColumn(name = "durata_i_um")

    private T_UMT umtDurataImp;

    @ManyToOne
    @JoinColumn(name = "allarme_un")

    private T_UMT umtAllImp;

    @OneToMany(mappedBy = "impegno")
    @JsonIgnore
    private List<T_Evento> eventi;


}
