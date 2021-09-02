package com.leiloes.sistemadeleiloes.integracao.service;

import com.leiloes.sistemadeleiloes.model.Lance;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.LanceRepository;
import com.leiloes.sistemadeleiloes.repository.LeilaoRepository;
import com.leiloes.sistemadeleiloes.repository.UsuarioRepository;
import com.leiloes.sistemadeleiloes.service.LanceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class LanceServiceIntegrationTest {

    @Autowired
    private LanceService lanceService;

    @Autowired
    private LanceRepository lanceRepository;

    @Autowired
    private LeilaoRepository leilaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Leilao leilao;
    private Usuario usuario;

    @Test
    public void fazLance() {
        LocalDate date = LocalDate.parse("2021-10-21");
        Date dataExpiracao = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        leilao = new Leilao("TESTE", "ABERTO", new BigDecimal(500), dataExpiracao);
        usuario = new Usuario("João Neto", "111.111.111-50", "teste@gmail.com", "joao");

        Lance lance = new Lance(leilao, new BigDecimal(600), usuario);
        String r = lanceService.fazLance(lance);

        Leilao leilaoEncontrado = leilaoRepository.findLeilaoByItem("TESTE");

        afterEach(lance, usuario, leilao);

        Assertions.assertEquals("SUCCESS", r);
        Assertions.assertEquals(leilao.getItem(), leilaoEncontrado.getItem());
        Assertions.assertEquals(leilao.getLance().getLeilao().getItem(), leilaoEncontrado.getLance().getLeilao().getItem());
    }

    @AfterEach
    public void afterEach(Lance lance, Usuario usuario, Leilao leilao) {
        leilaoRepository.nulaOLanceEmLeilao(leilao.getId());

        lanceRepository.delete(lance);
        leilaoRepository.delete(leilao);
        usuarioRepository.delete(usuario);
    }

}
