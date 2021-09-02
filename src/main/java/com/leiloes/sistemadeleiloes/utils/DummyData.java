package com.leiloes.sistemadeleiloes.utils;

import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.repository.RolesRepository;
import com.leiloes.sistemadeleiloes.repository.UsuarioRepository;
import com.leiloes.sistemadeleiloes.service.LanceService;
import com.leiloes.sistemadeleiloes.service.LeilaoService;
import com.leiloes.sistemadeleiloes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //@PostConstruct
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

    }
}

/*
insert into leiloes_tb(id, data_expiracao, item, status, valor_lance_minimo)
values(1, '22-05-10', 'PS5', 'FINALIZADO', 50.00);

insert into leiloes_tb(id, data_expiracao, item, status, valor_lance_minimo)
values(2, '23-04-5', 'Frigideira', 'ABERTO', 90.0);

insert into leiloes_tb(id, data_expiracao, item, status, valor_lance_minimo)
values(1, '22-05-10', 'ps5', 'FINALIZADO', 50.00);

insert into leiloes_tb(id, data_expiracao, item, status, valor_lance_minimo)
values(1, '22-05-10', 'ps5', 'FINALIZADO', 50.00);

insert into leiloes_tb(id, data_expiracao, item, status, valor_lance_minimo)
values(1, '22-05-10', 'ps5', 'FINALIZADO', 50.00);


insert into participantes_tb(id, nome, cpf, email)
values(1, 'João Lopes Santana Neto', '000.031.991-80', 'joaoneto@gmail.com');

insert into participantes_tb(id, nome, cpf, email)
values(1, 'João Lopes Santana Neto', '000.031.991-80', 'joaoneto@gmail.com');

insert into participantes_tb(id, nome, cpf, email)
values(2, 'Marta Maria', '002.032.992-80', 'martamaria@gmail.com');

insert into participantes_tb(id, nome, cpf, email)
values(3, 'Wilson Naufrago', '003.033.993-80', 'wilsonabola@gmail.com');

insert into lances_tb(id, valor, leilao_id, participante_id)
values(2, 2100, 1, 1);

insert into lances_tb(id, valor, leilao_id, usuario_id)
values(2, 3000, 72, 80);
 */

