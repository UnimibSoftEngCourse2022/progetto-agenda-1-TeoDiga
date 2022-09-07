package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOImpegno {
    public Character CRUD;
    public Integer id;
    public String titolo;
    public String descrizione;
    public String tipo;
    public String freq;
    public Integer freqN;
    public String durata;
    public Integer durataN;
    public String all;
    public Integer allN;
    public String inizioImpegno;
    public String fineImpegno;


}
