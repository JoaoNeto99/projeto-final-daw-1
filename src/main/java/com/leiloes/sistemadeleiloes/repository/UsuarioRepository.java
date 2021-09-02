package com.leiloes.sistemadeleiloes.repository;

import com.leiloes.sistemadeleiloes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM usuarios_tb p WHERE p.email = ?1", nativeQuery = true)
    Usuario findByEmail(String email);

    @Modifying
    @Query(value = "insert into usuarios_tb_roles_tb(usuario_id, role_id) values(:usuario_id, 'ROLE_USER')", nativeQuery = true)
    @Transactional
    void giveUserPermission(@Param("usuario_id") long id);

}
