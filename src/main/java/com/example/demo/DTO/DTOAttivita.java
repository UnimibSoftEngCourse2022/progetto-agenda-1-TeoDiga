package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOAttivita {
    public String titolo;
    public String descrizione;
    public String tipo;
    public String creazione;
    public String scadenza;
    public String durata;
    public Integer durataN;
    public String allarme;
    public Integer allarmeN;
}
