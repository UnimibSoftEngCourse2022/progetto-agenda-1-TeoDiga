package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TIPI_EVENTO", schema = "APP")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class T_Tipo_evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_impegno")
    public Integer idTipoEvento;
    @ManyToOne
    @JoinColumn(name = "id_utente")
    @JsonIgnore
    private T_Utente utenteTe;

    public  String tipo, descrizione, colore;

    @OneToMany(mappedBy = "tipoEventoImp")
    private List<T_Impegno> impegni;
    @OneToMany(mappedBy = "tipoEventoAtt")
    private List<T_Attivita> attivitas;


}
