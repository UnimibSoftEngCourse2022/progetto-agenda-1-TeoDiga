package com.example.demo.controller;

import com.example.demo.model.T_UMT;
import com.example.demo.repository.UMT_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/UMT")
public class UMT_controller {
    @Autowired
    private UMT_repository umt_rep;
    @GetMapping
    public List<T_UMT> getUMT(){
        return umt_rep.findAll();

    }

}
