package com.leiloes.sistemadeleiloes.service;

import com.leiloes.sistemadeleiloes.model.Leilao;

import java.util.ArrayList;
import java.util.List;

public interface LeilaoService  {

    List<Leilao> findAll();
    List<Leilao> findLeilaoAbertos();
    List<Leilao> findLeilaoByStatus(String status);
    Leilao findById(long id);
    Leilao save(Leilao leilao);
    Leilao update(Leilao leilao);
    String delete(long id);
    ArrayList<Leilao> expiraComData();
    void finalizaTodosExpirado();
    String finalizaLeilao(long id);
    Leilao abreLeilao(long id);

}
