package com.leiloes.sistemadeleiloes.unitario.service;

import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.UsuarioRepository;
import com.leiloes.sistemadeleiloes.service.serviceImpl.UsuarioServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class UsuarioServiceTest {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @Before
    public void setup() {
        usuario = new Usuario("João Neto", "111.111.111-50", "joaoneto7499@gmail.com", "joao");
    }

    @Test
    public void criptografaSenha() {
        usuario.setId(5);
        Mockito.when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);
        Usuario usuarioRetornado = usuarioService.save(usuario);
        System.out.println(usuarioRetornado.getAuthorities());
        Assertions.assertNotEquals("joao", usuarioRetornado.getSenha());
    }


}
