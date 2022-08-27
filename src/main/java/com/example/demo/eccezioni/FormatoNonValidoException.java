package com.example.demo.eccezioni;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class FormatoNonValidoException extends RuntimeException {

    public FormatoNonValidoException (String testo){
        super(testo);
    }
}
