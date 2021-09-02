package com.leiloes.sistemadeleiloes.ui;

import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.UsuarioRepository;
import com.leiloes.sistemadeleiloes.service.UsuarioService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class HomeTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private WebDriver driver;


    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void clicaNoBotaoRegistrar() {
        driver.get("http://localhost:8080/");
        driver.manage().window().setSize(new Dimension(1366, 728));
        driver.findElement(By.linkText("Registre-se")).click();
        String url = driver.getCurrentUrl();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("http://localhost:8080/registrar", url);
    }

    @Test
    public void registraParticipante() {
        driver.get("http://localhost:8080/");
        driver.manage().window().setSize(new Dimension(1364, 726));
        driver.findElement(By.linkText("Registre-se")).click();
        driver.findElement(By.id("floatingNome")).click();
        driver.findElement(By.id("floatingNome")).sendKeys("João Neto");
        driver.findElement(By.id("floatingEmail")).click();
        driver.findElement(By.id("floatingEmail")).sendKeys("joaoneto7499@email.com");
        driver.findElement(By.id("floatingCPF")).click();
        driver.findElement(By.id("floatingCPF")).sendKeys("002.032.922-80");
        driver.findElement(By.id("floatingSenha")).click();
        driver.findElement(By.id("floatingSenha")).sendKeys("joaoneto");
        driver.findElement(By.cssSelector(".btn")).click();

        String url = driver.getCurrentUrl();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Usuario usuarioCadastrado = usuarioService.findByEmail("joaoneto7499@email.com");
        excluiDados(usuarioCadastrado);
        Assertions.assertEquals("http://localhost:8080/", url);
        Assertions.assertEquals("joaoneto7499@email.com", usuarioCadastrado.getEmail());
    }

    private void excluiDados(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }

}
