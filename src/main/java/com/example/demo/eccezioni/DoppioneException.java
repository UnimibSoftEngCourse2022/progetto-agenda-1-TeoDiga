package com.example.demo.eccezioni;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
public class DoppioneException extends RuntimeException{

    public DoppioneException(String testo) {
        super(testo);
    }
}
