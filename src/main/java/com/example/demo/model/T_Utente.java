package com.example.demo.model;

import lombok.*;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "UTENTI",schema = "APP")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class T_Utente {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_utente")
    public Integer idUtente;

    @Column(name = "user_id")
    public String userName;
    @Column(name ="nome_cognome")
    public String nomeCognome;
    public String password;


    @OneToMany(mappedBy = "utenteTc")
    private List<T_Tipo_contatto> tipi_contatto;
    @OneToMany(mappedBy = "utenteCon")
    private List<T_Contatto> contatti;
    @OneToMany(mappedBy = "utenteTe")
    private List<T_Tipo_evento> tipi_evento;
    @OneToMany(mappedBy = "utenteImp")
    private List<T_Impegno> impegni;
    @OneToMany(mappedBy = "utenteAtt")
    private List<T_Attivita> attività;
    @OneToMany(mappedBy = "utenteEv")
    private List<T_Evento> eventi;

}
