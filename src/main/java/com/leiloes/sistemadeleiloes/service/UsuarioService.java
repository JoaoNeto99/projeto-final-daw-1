package com.leiloes.sistemadeleiloes.service;

import com.leiloes.sistemadeleiloes.model.Usuario;

import java.util.List;

public interface UsuarioService {

    List<Usuario> findAll();
    Usuario findById(long id);
    Usuario findByEmail(String email);
    Usuario save(Usuario usuario);
    Usuario giveUserPermission(Usuario usuario);
    void delete(long id);
}
