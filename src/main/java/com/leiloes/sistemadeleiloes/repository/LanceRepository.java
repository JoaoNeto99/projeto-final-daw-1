package com.leiloes.sistemadeleiloes.repository;

import com.leiloes.sistemadeleiloes.model.Lance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LanceRepository extends JpaRepository<Lance, Long> {

    @Query(value = "SELECT * FROM lances_tb l WHERE l.leilao_id = ?1", nativeQuery = true)
    public List<Lance> findByLeilao(long id);

    @Modifying
    @Query(value = "delete from lances_tb where id = ?1", nativeQuery = true)
    @Transactional
    void excluiLancePorId(long id);

}
