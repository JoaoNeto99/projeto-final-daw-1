package com.leiloes.sistemadeleiloes.repository;

import com.leiloes.sistemadeleiloes.model.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LeilaoRepository extends JpaRepository<Leilao, Long> {

    @Query(value = "SELECT * FROM leiloes_tb l WHERE l.status = 'ABERTO'", nativeQuery = true)
    public List<Leilao> findLeilaoAbertos();

    @Query(value = "SELECT * FROM leiloes_tb l WHERE l.status = ?1", nativeQuery = true)
    public List<Leilao> findLeilaoByStatus(String status);

    @Query(value = "SELECT * FROM leiloes_tb l WHERE l.item = ?1", nativeQuery = true)
    public Leilao findLeilaoByItem(String nome);

    @Modifying
    @Query(value = " update leiloes_tb set lance_id = NULL where id = ?1", nativeQuery = true)
    @Transactional
    void nulaOLanceEmLeilao(long id);

    @Modifying
    @Query(value = " update leiloes_tb set lance_id = ?1 where id = ?2", nativeQuery = true)
    @Transactional
    void atribuiLanceAoLeilao(long id, long idDoLeilao);
}
