package com.leiloes.sistemadeleiloes.service;

import com.leiloes.sistemadeleiloes.model.Lance;

import java.util.List;

public interface LanceService {

    List<Lance> findAll();
    Lance findById(long id);
    Lance save(Lance lance);
    List<Lance> findByLeilao(long idLeilao);
    String fazLance(Lance lance);
}
