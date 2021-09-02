package com.leiloes.sistemadeleiloes.ui;

import com.leiloes.sistemadeleiloes.model.Lance;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.LanceRepository;
import com.leiloes.sistemadeleiloes.repository.LeilaoRepository;
import com.leiloes.sistemadeleiloes.repository.UsuarioRepository;
import com.leiloes.sistemadeleiloes.service.LanceService;
import com.leiloes.sistemadeleiloes.service.LeilaoService;
import com.leiloes.sistemadeleiloes.service.UsuarioService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class LeilaoAdmTest {

    private WebDriver driver;

    @Autowired
    private LeilaoService leilaoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LanceService lanceService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LeilaoRepository leilaoRepository;

    @Autowired
    private LanceRepository lanceRepository;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        fazLogin();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void abreLeilao() {
        Leilao leilao = criaLeilao();

        driver.get("http://localhost:8080/leilao/list");
        driver.findElement(By.cssSelector("tr:last-child .btn-outline-success")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Leilao leilaoAlterado = leilaoService.findById(leilao.getId());
        excluiDados(leilaoAlterado);
        Assertions.assertEquals("ABERTO", leilaoAlterado.getStatus());
    }

    @Test
    public void finalizaLeilao() {
        Lance lance = lanceRepository.save(new Lance(
                new Leilao("TESTESELENIUM", "ABERTO", new BigDecimal(500), null),
                new BigDecimal(1000),
                new Usuario("Teste", "123456789", "teste@email.com", "teste")));
        leilaoRepository.atribuiLanceAoLeilao(lance.getId(), lance.getLeilao().getId());
        driver.get("http://localhost:8080/leilao/list");
        driver.findElement(By.cssSelector("tr:first-child .btn-outline-secondary")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Leilao leilaoAlterado = leilaoService.findById(lance.getLeilao().getId());
        excluiDados(lance);
        Assertions.assertEquals("FINALIZADO", leilaoAlterado.getStatus());
    }

    @Test
    public void alteraLeilao() throws MalformedURLException {
        Leilao leilao = criaLeilao();
        driver.get("http://localhost:8080/leilao/list");
        driver.findElement(By.cssSelector("tr:last-child .btn-outline-primary")).click();
        String url = driver.getCurrentUrl();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL uri1 = new URL(url);
        excluiDados(leilao);
        String onlyUrl=url.substring(0,url.lastIndexOf("/"));
        Assertions.assertEquals("http://localhost:8080/leilao/update", onlyUrl);
    }

    @Test
    public void deletaLeilao() {
        Leilao leilao = criaLeilao();
        driver.get("http://localhost:8080/leilao/list");
        List<WebElement> node1 =  driver.findElements(By.cssSelector("tr"));
        driver.findElement(By.cssSelector("tr:last-child .btn-outline-danger")).click();
        List<WebElement> node2 = driver.findElements(By.cssSelector("tr"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(node1.size()-1, node2.size());
    }

    @Test
    public void vaiParaPaginaDeLance() {
        driver.get("http://localhost:8080/leilao/list");
        driver.findElement(By.linkText("Lances")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String url = driver.getCurrentUrl();

        Assertions.assertEquals("http://localhost:8080/lance/list", url);
    }

    @Test
    public void vaiParaPaginaDeParticipantes() {
        driver.get("http://localhost:8080/leilao/list");
        driver.findElement(By.linkText("Participantes")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String url = driver.getCurrentUrl();

        Assertions.assertEquals("http://localhost:8080/usuario/list", url);
    }

    @Test
    public void botaoDeNovoLeilao() {
        driver.get("http://localhost:8080/leilao/list");
        driver.findElement(By.linkText("Novo Leilão")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String url = driver.getCurrentUrl();

        Assertions.assertEquals("http://localhost:8080/leilao/new", url);
    }

    @Test
    public void salvaNovoLeilao() {
        driver.get("http://localhost:8080/leilao/list");
        List<WebElement> listaInicialDeLeiloes =  driver.findElements(By.cssSelector("tr"));

        driver.findElement(By.linkText("Novo Leilão")).click();
        driver.findElement(By.id("floatingItem")).click();
        driver.findElement(By.id("floatingItem")).sendKeys("testeSelenium");
        driver.findElement(By.id("floatingValorMinimo")).click();
        driver.findElement(By.id("floatingValorMinimo")).sendKeys("2000");
        driver.findElement(By.id("floatingData")).click();
        driver.findElement(By.id("floatingData")).sendKeys("2021-09-01");
        driver.findElement(By.cssSelector(".btn-primary")).click();

        List<WebElement> listaAtualizadaDeLeiloes =  driver.findElements(By.cssSelector("tr"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Leilao leilao = leilaoRepository.findLeilaoByItem("testeSelenium");
        excluiDados(leilao);
        Assertions.assertEquals(listaInicialDeLeiloes.size() +1, listaAtualizadaDeLeiloes.size());
    }

    @Test
    public void filtraLeiloesPeloStatusAberto() {
        Leilao leilao = criaLeilao();
        driver.get("http://localhost:8080/leilao/list");
        driver.findElement(By.id("floatingSelect")).click();
        {
            WebElement dropdown = driver.findElement(By.id("floatingSelect"));
            dropdown.findElement(By.xpath("//option[. = 'Inativo']")).click();
        }
        driver.findElement(By.cssSelector(".btn-primary")).click();
        List<WebElement> quantidadeDeLinhas =  driver.findElements(By.cssSelector("tr"));

        excluiDados(leilao);
        Assertions.assertNotEquals(1, quantidadeDeLinhas.size());
    }

    @Test
    public void filtraLancesPeloLeilao() {
        Lance lance = criaLance();

        driver.get("http://localhost:8080/leilao/list");
        driver.manage().window().setSize(new Dimension(1382, 744));
        driver.findElement(By.linkText("Lances")).click();
        {
            WebElement dropdown = driver.findElement(By.id("floatingSelect"));
            dropdown.findElement(By.xpath("//option[. = 'TESTESELENIUM']")).click();
        }
        driver.findElement(By.cssSelector(".btn")).click();

        List<WebElement> quantidadeDeLinhas =  driver.findElements(By.cssSelector("tr"));
        excluiDados(lance);
        Assertions.assertEquals(2, quantidadeDeLinhas.size());
    }

    private void fazLogin() {
        driver.get("http://localhost:8080/");
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("admin@email.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.cssSelector(".btn")).click();
    }



    private Leilao criaLeilao() {
        LocalDate date = LocalDate.parse("2021-10-21");
        Date dataExpiracao = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return leilaoService.save(new Leilao("TESTESELENIUM", "ABERTO", new BigDecimal(500), dataExpiracao));
    }

    private Lance criaLance() {
        Lance lance = lanceRepository.save(new Lance(
                new Leilao("TESTESELENIUM", "ABERTO", new BigDecimal(500), null),
                new BigDecimal(1000),
                new Usuario("Teste", "123456789", "teste@email.com", "teste")));
        return lance;
    }

    private Usuario criaUsuario() {
        return usuarioService.save(new Usuario("Teste", "123456789", "teste@email.com", "teste"));
    }

    private void excluiDados(Leilao leilao) {
        leilaoService.delete(leilao.getId());
    }

    private void excluiDados(Lance lance) {
        lanceRepository.delete(lance);
        leilaoRepository.delete(lance.getLeilao());
        usuarioRepository.delete(lance.getUsuario());
    }

//    private void excluiDados(Lance lance, Leilao leilao, Usuario usuario) {
//        leilaoService.delete(leilao.getId());
//
//    }
}
