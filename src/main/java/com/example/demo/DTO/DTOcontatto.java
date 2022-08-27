package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DTOcontatto {
    public Integer id;
    public String tipo;
    public String nomeCognome;
    public String email;
    public String telefono;
}
