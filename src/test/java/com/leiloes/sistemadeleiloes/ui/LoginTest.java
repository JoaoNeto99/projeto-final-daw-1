package com.leiloes.sistemadeleiloes.ui;

import com.leiloes.sistemadeleiloes.model.Role;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.RolesRepository;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class LoginTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolesRepository rolesRepository;

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
    public void fazLoginComoUsuario() {
        Usuario usuario = new Usuario("Teste", "123456789", "teste@email.com", "teste");
        usuario = usuarioService.save(usuario);

        driver.get("http://localhost:8080/");
        driver.manage().window().setSize(new Dimension(1364, 726));
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("teste@email.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("teste");
        driver.findElement(By.cssSelector(".btn")).click();

        String url = driver.getCurrentUrl();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        excluiDados(usuario);
        Assertions.assertEquals("http://localhost:8080/leilao_list", url);
    }

    @Test
    public void fazLoginComoAdm() {
        driver.get("http://localhost:8080/");
        driver.manage().window().setSize(new Dimension(1364, 726));
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("admin@email.com");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.cssSelector(".btn")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = driver.getCurrentUrl();

        Assertions.assertEquals("http://localhost:8080/leilao/list", url);
    }

    private void excluiDados(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }
}
