package com.leiloes.sistemadeleiloes.repository;

import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolesRepository extends JpaRepository<Role, String> {

    @Modifying
    @Query(value = "insert into roles_tb(nome_role) values('ROLE_USER')", nativeQuery = true)
    @Transactional
    void addRoleUser();

    @Modifying
    @Query(value = "insert into roles_tb(nome_role) values('ROLE_ADMIN')", nativeQuery = true)
    @Transactional
    void addRoleAdm();

    @Modifying
    @Query(value = "insert into usuarios_tb_roles_tb(usuario_id, role_id) values(?1,'ROLE_ADMIN')", nativeQuery = true)
    @Transactional
    void addADM(long id);

    @Modifying
    @Query(value = "delete from usuarios_tb_roles_tb where usuario_id = ?1", nativeQuery = true)
    @Transactional
    void deleteUserRole(long id);

}
