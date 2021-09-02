package com.leiloes.sistemadeleiloes.ui;

import com.leiloes.sistemadeleiloes.model.Lance;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Role;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.LanceRepository;
import com.leiloes.sistemadeleiloes.repository.LeilaoRepository;
import com.leiloes.sistemadeleiloes.repository.RolesRepository;
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

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class LeilaoUserTest {

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

    @Autowired
    private RolesRepository rolesRepository;


    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        fazLogin();
    }

    @After
    public void tearDown() {
        driver.quit();
        Usuario usuario = usuarioRepository.findByEmail("contateste@email.com");
        rolesRepository.deleteUserRole(usuario.getId());
        usuarioRepository.delete(usuario);
    }

    @Test
    public void acessaOsLancesDeUmLeilao() throws MalformedURLException {
        Lance lance = lanceRepository.save(new Lance(
                new Leilao("TESTESELENIUM", "ABERTO", new BigDecimal(500), null),
                new BigDecimal(1000),
                new Usuario("Teste", "123456789", "teste@email.com", "teste")));
        driver.get("http://localhost:8080/leilao_list");
        driver.findElement(By.cssSelector("tr:last-child .btn-outline-primary")).click();
        String url = driver.getCurrentUrl();
        excluiDados(lance);
        URL uri1 = new URL(url);
        String onlyUrl=url.substring(0,url.lastIndexOf("/"));
        Assertions.assertEquals("http://localhost:8080/lance_list", onlyUrl);
    }

    private void excluiDados(Lance lance) {
        lanceRepository.delete(lance);
        leilaoRepository.delete(lance.getLeilao());
        usuarioRepository.delete(lance.getUsuario());
    }

    private void fazLogin() {
        usuarioService.save(new Usuario("ContaTeste", "00000000", "contateste@email.com", "teste"));
        driver.get("http://localhost:8080/");
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("contateste@email.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("teste");
        driver.findElement(By.cssSelector(".btn")).click();
    }

}
