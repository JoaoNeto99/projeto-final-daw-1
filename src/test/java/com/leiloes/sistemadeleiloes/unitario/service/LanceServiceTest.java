package com.leiloes.sistemadeleiloes.unitario.service;

import com.leiloes.sistemadeleiloes.model.Lance;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.LanceRepository;
import com.leiloes.sistemadeleiloes.repository.LeilaoRepository;
import com.leiloes.sistemadeleiloes.service.LanceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class LanceServiceTest {

    @Autowired
    private LanceService lanceService;

    @MockBean
    private LanceRepository lanceRepository;

    @MockBean
    private LeilaoRepository leilaoRepository;

    private Leilao leilao;
    private Usuario usuario;

    @Before
    public void setup() {
        LocalDate date = LocalDate.parse("2021-10-21");
        Date dataExpiracao = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

        leilao = new Leilao("PS5", "ABERTO", new BigDecimal(500), dataExpiracao);
        usuario = new Usuario("João Neto", "111.111.111-50", "joaoneto7499@gmail.com", "joao");
    }

    @Test
    public void lanceNaoPodeSerMenorQueValorLeilao() {
        Lance lance = new Lance(leilao, new BigDecimal(400), usuario);

        String r = lanceService.fazLance(lance);
        Assertions.assertEquals("ERROR:01 - lance menor que valor minimo de leilao.", r);
        Mockito.verifyNoInteractions(lanceRepository);
        Mockito.verifyNoInteractions(leilaoRepository);
    }

    @Test
    public void umNovoLanceNaoPodeSerIgualOuMenorAoUltimoLance() {
        Lance primeiroLance = new Lance(leilao, new BigDecimal(550), usuario);
        String r1 = lanceService.fazLance(primeiroLance);

        Usuario segundoUsuario = new Usuario("Maria", "121.121.121-20", "maria@gmail.com", "maria");
        Lance segundoLance = new Lance(leilao, new BigDecimal(550), segundoUsuario);

        String r2 = lanceService.fazLance(segundoLance);

        Assertions.assertEquals("ERROR:02 - lance nao pode ser menor que o ultimo lance.", r2);
    }

    @Test
    public void umParticipanteNaoPodeFazerLanceSeguidos() {
        Lance primeiroLance = new Lance(leilao, new BigDecimal(550), usuario);
        String r1 = lanceService.fazLance(primeiroLance);
        Lance segundoLance = new Lance(leilao, new BigDecimal(600), usuario);

        String r2 = lanceService.fazLance(segundoLance);
        Assertions.assertEquals("ERROR:03 - nao e permitido fazer dois lances seguidos.", r2);
    }
    
}
