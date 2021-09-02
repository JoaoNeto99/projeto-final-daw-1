package com.leiloes.sistemadeleiloes.integracao.service;

import com.leiloes.sistemadeleiloes.email.EmailService;
import com.leiloes.sistemadeleiloes.model.Lance;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.LanceRepository;
import com.leiloes.sistemadeleiloes.repository.LeilaoRepository;
import com.leiloes.sistemadeleiloes.repository.UsuarioRepository;
import com.leiloes.sistemadeleiloes.service.LanceService;
import com.leiloes.sistemadeleiloes.service.LeilaoService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class LeilaoServiceIntegrationTest {

    @Autowired
    private LeilaoService leilaoService;

    @Autowired
    private LeilaoRepository leilaoRepository;

    @Autowired
    private LanceService lanceService;

    @Autowired
    private LanceRepository lanceRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Leilao leilao;
    private Usuario usuario;
    private Lance lance;

    @Test
    public void salvaLeilaoAberto() {
        LocalDate date = LocalDate.parse("2021-10-21");
        Date dataExpiracao = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        leilao = new Leilao("PS5", "ABERTO", new BigDecimal(500), dataExpiracao);
        leilaoService.save(leilao);

        Leilao leilaoEncontrado = leilaoRepository.findLeilaoByItem(leilao.getItem());
        Assertions.assertEquals(leilao.getItem(), leilaoEncontrado.getItem());

        afterEach(leilao);
    }

    @Test
    public void deleteLeilao() {
        LocalDate date = LocalDate.parse("2021-10-21");
        Date dataExpiracao = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        leilao = new Leilao("PS5", "ABERTO", new BigDecimal(500), dataExpiracao);
        leilaoService.save(leilao);

        Leilao leilaoEncontrado = leilaoRepository.findLeilaoByItem(leilao.getItem());

        leilaoService.delete(leilaoEncontrado.getId());

        leilaoEncontrado = leilaoRepository.findLeilaoByItem(leilao.getItem());
        afterEach(leilao);
        Assertions.assertEquals(null, leilaoEncontrado);
    }

    @Test
    public void finalizaLeilao() {
        LocalDate date = LocalDate.parse("2021-10-21");
        Date dataExpiracao = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        leilao = new Leilao("PS5", "ABERTO", new BigDecimal(500), dataExpiracao);
        usuario = new Usuario("João Neto", "111.111.111-50", "joaoneto7499@gmail.com", "joao");
        lance = new Lance(leilao, new BigDecimal(600), usuario);
        leilao.setLance(lance);
        leilaoService.save(leilao);

        Leilao leilaoEncontrado = leilaoRepository.findLeilaoByItem(leilao.getItem());
        leilaoService.abreLeilao(leilaoEncontrado.getId());

        leilaoEncontrado = leilaoRepository.findLeilaoByItem(leilao.getItem());
        String r = leilaoService.finalizaLeilao(leilaoEncontrado.getId());

        leilaoEncontrado = leilaoRepository.findLeilaoByItem(leilao.getItem());

        Assertions.assertEquals("SUCCESS", r);
        Assertions.assertEquals("FINALIZADO", leilaoEncontrado.getStatus());

        afterEach(lance, usuario, leilao);
    }

    @Test
    public void abreLeilao() {
        LocalDate date = LocalDate.parse("2021-10-21");
        Date dataExpiracao = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        leilao = new Leilao("PS5", "ABERTO", new BigDecimal(500), dataExpiracao);
        leilaoService.save(leilao);

        Leilao leilaoEncontrado = leilaoRepository.findLeilaoByItem(leilao.getItem());
        leilaoService.abreLeilao(leilaoEncontrado.getId());
        leilaoEncontrado = leilaoRepository.findLeilaoByItem(leilao.getItem());

        Assertions.assertEquals("ABERTO", leilaoEncontrado.getStatus());

        afterEach(leilaoEncontrado);
    }

    public void afterEach(Leilao leilao) {
        leilaoRepository.delete(leilao);
    }

    public void afterEach(Lance lance, Usuario usuario, Leilao leilao) {
        lanceRepository.delete(lance);
        leilaoRepository.delete(leilao);
        usuarioRepository.delete(usuario);
    }

}
