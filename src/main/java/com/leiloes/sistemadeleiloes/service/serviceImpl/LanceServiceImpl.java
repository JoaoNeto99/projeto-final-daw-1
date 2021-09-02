package com.leiloes.sistemadeleiloes.service.serviceImpl;

import com.leiloes.sistemadeleiloes.model.Lance;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.repository.LanceRepository;
import com.leiloes.sistemadeleiloes.service.LanceService;
import com.leiloes.sistemadeleiloes.service.LeilaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanceServiceImpl implements LanceService {

    @Autowired
    LanceRepository lanceRepository;

    @Autowired
    LeilaoService leilaoService;

    @Override
    public List<Lance> findAll() {
        return lanceRepository.findAll();
    }

    @Override
    public Lance findById(long id) {
        return lanceRepository.findById(id).get();
    }

    @Override
    public Lance save(Lance lance) {
        return lanceRepository.save(lance);
    }

    @Override
    public List<Lance> findByLeilao(long idLeilao) {
        return lanceRepository.findByLeilao(idLeilao);
    }

    public String fazLance(Lance lance) {
        Leilao leilao = lance.getLeilao();

        if(lanceNaoPodeSerMenorQueValorLeilao(leilao, lance)) {
            if(umNovoLanceNaoPodeSerIgualOuMenorAoUltimoLance(leilao, lance)) {
                if (umParticipanteNaoPodeFazerLanceSeguidos(leilao, lance)) {
                        save(lance);
                        leilao.setLance(lance);
                        leilaoService.update(leilao);
                }  else { return "ERROR:03 - nao e permitido fazer dois lances seguidos."; }
            } else { return "ERROR:02 - lance nao pode ser menor que o ultimo lance."; }
        } else { return "ERROR:01 - lance menor que valor minimo de leilao."; }
        return "SUCCESS";
    }

    private boolean lanceNaoPodeSerMenorQueValorLeilao(Leilao leilao, Lance lance){ //8

        boolean aprovou = false;
        if(leilao.getValorLanceMinimo().compareTo(lance.getValor()) < 0 ){
            aprovou = true;
        }
        return aprovou;
    }

    private boolean umNovoLanceNaoPodeSerIgualOuMenorAoUltimoLance(Leilao leilao, Lance lance) { //09
        boolean aprovou = false;
        if(leilao.getLance() == null){
            aprovou = true;
        } else if(lance.getValor().compareTo(leilao.getLance().getValor()) > 0){
            aprovou = true;
        }
        return aprovou;
    }

    private boolean umParticipanteNaoPodeFazerLanceSeguidos(Leilao leilao, Lance lance) { //10
        boolean aprovou = false;
        if(leilao.getLance() == null){
            aprovou = true;
        }else if(!leilao.getLance().getUsuario().getEmail().equals(lance.getUsuario().getEmail())){
            aprovou = true;
        }
        return aprovou;
    }
}
