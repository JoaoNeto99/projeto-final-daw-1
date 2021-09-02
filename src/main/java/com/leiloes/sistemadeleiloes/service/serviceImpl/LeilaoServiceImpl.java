package com.leiloes.sistemadeleiloes.service.serviceImpl;

import com.leiloes.sistemadeleiloes.email.EmailService;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.repository.LeilaoRepository;
import com.leiloes.sistemadeleiloes.service.LeilaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LeilaoServiceImpl implements LeilaoService {

    @Autowired
    private LeilaoRepository leilaoRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Leilao> findAll() {
        return leilaoRepository.findAll();
    }

    @Override
    public List<Leilao> findLeilaoAbertos() {
        return leilaoRepository.findLeilaoAbertos();
    }

    @Override
    public List<Leilao> findLeilaoByStatus(String status) {
        return leilaoRepository.findLeilaoByStatus(status);
    }

    @Override
    public Leilao findById(long id) {
        return leilaoRepository.findById(id).get();
    }

    @Override
    public Leilao save(Leilao leilao) {
        leilao.setStatus("INATIVO");
        return leilaoRepository.save(leilao);
    }

    @Override
    public Leilao update(Leilao leilao) {
        return leilaoRepository.save(leilao);
    }

    @Override
    public String delete(long id) {
        Leilao leilao = findById(id);
        System.out.println(leilao.getId() +"  " + leilao.getStatus() + " " + leilao.getLance());
        if(leilao.getLance() == null) {
            System.out.println("entrou");
            leilaoRepository.deleteById(id);
            return "SUCCESS";
        } else {
            return "FAIL";
        }
    }

    @Override
    public ArrayList<Leilao> expiraComData() {
        Date data = new Date();
        List<Leilao> list = findAll();
        ArrayList<Leilao> listExpirada = new ArrayList<>();
        for (Leilao l : list) {
            if (l.getDataExpiracao().before(data)) {
                l.setStatus("EXPIRADO");
                update(l);
                listExpirada.add(l);
            }
        }
        return listExpirada;
    }

    @Override
    public void finalizaTodosExpirado() {
        List<Leilao> list = findAll();
        for (Leilao l : list) {
            if (l.getStatus().equals("EXPIRADO")) {
                l.setStatus("FINALIZADO");
                update(l);
            }
        }
    }

    @Override
    public String finalizaLeilao(long id) {
        Leilao leilao = findById(id);

        if(leilao.getLance() == null) {
            return "FAIL - SEM LANCE";
        }
        if (leilao.getStatus().equals("EXPIRADO") || leilao.getStatus().equals("ABERTO")) {
            leilao.setStatus("FINALIZADO");
            update(leilao);
            emailService.sendMail(leilao);
            return "SUCCESS";
        } else {
            return "FAIL";
        }
    }

    @Override
    public Leilao abreLeilao(long id) {
        Leilao leilao = findById(id);
        leilao.setStatus("ABERTO");
        return update(leilao);
    }

}
