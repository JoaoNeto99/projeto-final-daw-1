package com.leiloes.sistemadeleiloes.unitario.service;

import com.leiloes.sistemadeleiloes.email.EmailService;
import com.leiloes.sistemadeleiloes.model.Lance;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.LanceRepository;
import com.leiloes.sistemadeleiloes.repository.LeilaoRepository;
import com.leiloes.sistemadeleiloes.service.LanceService;
import com.leiloes.sistemadeleiloes.service.LeilaoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class LeilaoServiceTest {

    @Autowired
    private LeilaoService leilaoService;

    @MockBean
    private LeilaoRepository leilaoRepository;

    @MockBean
    private LanceService lanceService;

    @MockBean
    private LanceRepository lanceRepository;

    @MockBean
    private EmailService emailService;

    private Leilao leilao;
    private Usuario usuario;
    private Lance lance;

    @Before
    public void setup() {
        LocalDate date = LocalDate.parse("2021-10-21");
        Date dataExpiracao = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        leilao = new Leilao("PS5", "ABERTO", new BigDecimal(500), dataExpiracao);
        usuario = new Usuario("João Neto", "111.111.111-50", "joaoneto7499@gmail.com", "joao");
        lance = new Lance(leilao, new BigDecimal(600), usuario);
        Mockito.when(leilaoRepository.save(leilao)).thenReturn(leilao);
    }

    @Test
    public void salvaLeilaoInativo() {
        Leilao retornado = leilaoService.save(leilao);
        Assertions.assertEquals("INATIVO", retornado.getStatus());
    }

    @Test
    public void naoDeletaLeilaoQJaRecebeuLance() {
        leilao.setLance(lance);
        Mockito.when(leilaoRepository.findById(leilao.getId())).thenReturn(java.util.Optional.ofNullable(leilao));
        String result = leilaoService.delete(leilao.getId());

        Assertions.assertEquals("FAIL", result);
    }

    @Test
    public void expiraComData() {
        LocalDate date1 = LocalDate.parse("2021-05-21");
        Date dataExpiracao1 = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());

        LocalDate date2 = LocalDate.parse("2021-05-21");
        Date dataExpiracao2 = Date.from(date2.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Leilao leilao1 = new Leilao("IPHONE", "ABERTO", new BigDecimal(500), dataExpiracao1);
        Leilao leilao2 = new Leilao("TV", "ABERTO", new BigDecimal(500), dataExpiracao2);

        ArrayList<Leilao> list = new ArrayList<>();
        list.add(leilao1);
        list.add(leilao2);

        Mockito.when(leilaoService.findAll()).thenReturn(list);

        ArrayList retorno = leilaoService.expiraComData();

        Assertions.assertEquals("EXPIRADO",list.get(0).getStatus());
        Assertions.assertEquals("EXPIRADO",list.get(1).getStatus());
    }

    @Test
    public void finalizaLeilao() {
        leilao.setLance(lance);
        Mockito.when(leilaoRepository.findById(leilao.getId())).thenReturn(java.util.Optional.ofNullable(leilao));
        String r = leilaoService.finalizaLeilao(leilao.getId());

        Assertions.assertEquals("SUCCESS", r);
    }

    @Test
    public void abreLeilao() {
        Mockito.when(leilaoRepository.findById(leilao.getId())).thenReturn(java.util.Optional.ofNullable(leilao));
        Leilao retornado = leilaoService.abreLeilao(leilao.getId());
        Assertions.assertEquals("ABERTO", retornado.getStatus());
    }
}
