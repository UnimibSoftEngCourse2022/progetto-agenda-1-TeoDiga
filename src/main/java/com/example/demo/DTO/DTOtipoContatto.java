package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DTOtipoContatto {
    public Character CRUD;
    public Integer id;
    public String tipo;
    public String descrizione;
    public String colore;
}
