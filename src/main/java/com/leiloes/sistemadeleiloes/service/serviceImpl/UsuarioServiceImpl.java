package com.leiloes.sistemadeleiloes.service.serviceImpl;

import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.UsuarioRepository;
import com.leiloes.sistemadeleiloes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario findById(long id) {
        return usuarioRepository.findById(id).get();
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario save(Usuario usuario) {
        usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
        return giveUserPermission(usuario);
    }

    @Override
    public Usuario giveUserPermission(Usuario usuario) {
        Usuario usuarioEncontrado = findByEmail(usuario.getEmail());
        usuarioRepository.giveUserPermission(usuarioEncontrado.getId());
        return usuarioEncontrado;
    }

    @Override
    public void delete(long id) {
        usuarioRepository.deleteById(id);
    }
}
