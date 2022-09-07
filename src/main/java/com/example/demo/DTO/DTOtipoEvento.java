package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DTOtipoEvento {
    public String tipo;
    public String descrizione;
    public String colore;
    public Character CRUD;
    public Integer id;
}
