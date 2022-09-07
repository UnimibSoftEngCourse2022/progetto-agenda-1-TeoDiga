package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOEvento {
    public Integer id;
    public Integer att;
    public Integer imp;
    public Character CRUD;
    public String inizio;
    public String fine;
    public String sveglia;
    public String tipo;
    public String titolo;

}
