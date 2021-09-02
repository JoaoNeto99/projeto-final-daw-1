# Sistemas de Leilões

Este projeto foi desenvolvido nas matérias de Desenvolvimento de Aplicações Web e Testes de Aplicações. 

# Pré Requisitos
Ter instalado em sua máquina:
<ul>
<li>PostgreSQL</li>
<li>JRE e JDK</li>
<li>Intellij Idea ou Eclipse</li>
</ul>



# Tecnologias

<span><img src="https://img.shields.io/static/v1?label=Tech&message=Java&color=007396&style=for-the-badge&logo=Java"/> <span><img src="https://img.shields.io/static/v1?label=DataBase&message=Postgres&color=2F5E8D&style=for-the-badge&logo=PostgreSQL"/></span> <span><img src="https://img.shields.io/static/v1?label=Framework&message=Spring Boot&color=6DB33F&style=for-the-badge&logo=Spring"/></span><span><img src="https://img.shields.io/static/v1?label=Test&message=JUnit&color=25A162&style=for-the-badge&logo=JUnit5"/></span> <span><img src="https://img.shields.io/static/v1?label=Engine&message=Thymleaf&color=005F0F&style=for-the-badge&logo=Thymleaf"/></span>

## Configuração de Banco de Dados
Crie o banco de dados
```SQL
CREATE DATABASE leilao_db;
```
Atualize o arquivo <b>apllication.properties</b> com o <b>usuário e a senha</b> do seu PostgreSQL.
```properties
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=trueblog  
  
#Banco local - Leilao  
spring.datasource.url=jdbc:postgresql://localhost:5432/leilao_db  
spring.datasource.username=  
spring.datasource.password=  
spring.jpa.hibernate.ddl-auto=update  
  
#spring-boot-starter-mail properties  
spring.mail.host=smtp.outlook.com  
spring.mail.port=587  
spring.mail.username=leilao7499@outlook.com  
spring.mail.password=leilao123  
spring.mail.properties.mail.smtp.auth=true  
spring.mail.properties.mail.smtp.starttls.enable=true  
spring.mail.properties.mail.smtp.starttls.required=true  
spring.mail.properties.mail.smtp.ssl.enable=false  
spring.mail.test-connection=true```
```
## Preenchendo tabelas

Para a aplicação ser executada corretamente, é necessário que algumas tabelas estejam preenchidas, para isto, foi criado o arquivo <b>DummyData.java</b> presente no pacote <b>utils</b>, que automatizará a inserção de dados. 
Logo, na primeira vez em que o projeto for executado des-comente a linha
```java
@PostConstruct
```
Caso tenha dúvida, é assim que deve estar o arquivo.
```java
@Component  
public class DummyData {  
  
  @Autowired  
  LeilaoService leilaoService;  
  
    @Autowired  
  UsuarioService usuarioService;  
  
    @Autowired  
  LanceService lanceService;  
  
    @Autowired  
  RolesRepository rolesRepository;  
  
    @Autowired  
  UsuarioRepository usuarioRepository;  
  
    @PostConstruct //ESTA LINHA ESTARÁ COMENTADA, DESCOMENTE ELA. 
  public void save() {  
  rolesRepository.addRoleAdm();  
        rolesRepository.addRoleUser();  
        Usuario adm = new Usuario("Administrador", "000.000.000-00", "admin@email.com", new BCryptPasswordEncoder().encode("admin"));  
        usuarioRepository.save(adm);  
        Usuario uAdm = usuarioService.findByEmail("admin@email.com");  
        rolesRepository.addADM(uAdm.getId());  
  
  
        List<Leilao> listLeilao = new ArrayList<>();  
        Leilao l1 = new Leilao("Frigideira", "FINALIZADO", new BigDecimal(100), new Date());  
        Leilao l2 = new Leilao("Play Station 5", "ABERTO", new BigDecimal(1000), new Date());  
        Leilao l3 = new Leilao("Iphone", "INATIVO", new BigDecimal(1000), new Date());  
        Leilao l4 = new Leilao("Microondas", "ABERTO", new BigDecimal(100), new Date());  
        Leilao l5 = new Leilao("AK47", "ABERTO", new BigDecimal(1000), new Date());  
  
        listLeilao.add(l1);  
        listLeilao.add(l2);  
        listLeilao.add(l3);  
        listLeilao.add(l4);  
        listLeilao.add(l5);  
  
        for (Leilao l : listLeilao) {  
  Leilao leilaoSalvo = leilaoService.save(l);  
        }  
  List<Usuario> listUsuario = new ArrayList<>();  
        Usuario u1 = new Usuario("Marcos Alves", "111.111.111-11", "marcosalves@email.com", "marcosalves");  
        Usuario u2 = new Usuario("Elisa Maria", "222.222.222-22", "elisa@email.com", "elisa");  
        Usuario u3 = new Usuario("Rodrigo Faria", "333.333.333-33", "rodrigo@email.com", "rodrigo");  
  
        listUsuario.add(u1);  
        listUsuario.add(u2);  
        listUsuario.add(u3);  
  
        for (Usuario u : listUsuario) {  
  usuarioService.save(u);  
        }
```
Este código preenche as tabelas: Leilões, Usuários, Roles e UserRoles.

Agora execute o projeto, ao fazê-lo, será criado, configurado e preenchido sua base de dados. De modo que, ao terminar a execução, comente a linha abaixo no arquivo <b>DummyData.java</b> para que não seja executada a função save().
```java
@PostConstruct
```

## Credenciais

Foram criados alguns usuários ao se executar o arquivo DummyData.java, segue abaixo as suas credenciais para que você possa acessar o sistema.
<table border="1">
    <tr>
        <td>Nome</td>
        <td>E-mail</td>
        <td>Senha</td>
        <td>Role</td>
    </tr>
    <tr>
        <td>Administrador</td>
        <td>admin@email.com</td>
        <td>admin</td>
        <td>ADMIN</td>
    </tr>
    <tr>
        <td>Marcos Alves</td>
        <td>marcosalves@email.com</td>
        <td>marcosalves</td>
        <td>USER</td>
    </tr>
     <tr>
        <td>Elisa Maria</td>
        <td>elisa@email.com</td>
        <td>elisa</td>
        <td>USER</td>
    </tr>
     <tr>
        <td>Rodrigo Faria</td>
        <td>rodrigo@email.com</td>
        <td>rodrigo</td>
        <td>USER</td>
    </tr>
</table>

Caso queira criar outros usuários do tipo USER basta utilizar da funcionalidade <b>Registre-se</b>, porém, só existe um administrador, por isso, utilize das credenciais acima para logar como tal e acessar as funcionalidades administrativas.

## Contato

Em caso de dúvidas entre em contato com: joaoneto7499@gmail.com.
Envie o link do github do seu projeto, a stacktrace do erro e também uma descrição escrita da operação.
