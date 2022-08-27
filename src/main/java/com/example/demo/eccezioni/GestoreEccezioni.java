package com.example.demo.eccezioni;

import org.apache.derby.client.am.SqlException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;


@ControllerAdvice
public class GestoreEccezioni  extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { DoppioneException.class, NonTrovatoException.class, FormatoNonValidoException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(value = {  SQLException.class})
    protected ResponseEntity<Object> handleSQL(
           SQLException ex, WebRequest request) {


        String bodyOfResponse = "SQL: "+ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
