package com.example.demo.controller;

import com.example.demo.repository.Eventi_repository;
import com.example.demo.model.T_Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/eventi")
public class Eventi_controller {
    @Autowired
    private Eventi_repository ev_rep;
    @GetMapping
    public List<T_Evento> getEventi(){
        return ev_rep.findAll();
    }
}
