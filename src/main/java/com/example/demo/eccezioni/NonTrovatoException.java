package com.example.demo.eccezioni;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class NonTrovatoException  extends RuntimeException{

    public NonTrovatoException(String testo){
        super(testo);
    }
}
