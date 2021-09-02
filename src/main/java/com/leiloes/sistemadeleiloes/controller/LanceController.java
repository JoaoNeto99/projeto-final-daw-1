package com.leiloes.sistemadeleiloes.controller;

import com.leiloes.sistemadeleiloes.model.Lance;
import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.service.LanceService;
import com.leiloes.sistemadeleiloes.service.LeilaoService;
import com.leiloes.sistemadeleiloes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Controller
public class LanceController {

    @Autowired
    LanceService lanceService;

    @Autowired
    LeilaoService leilaoService;

    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(value = "/lance/list", method = RequestMethod.GET)
    public ModelAndView listAll() {
        ModelAndView mv = new ModelAndView("listlances_adm");
        List<Lance> lances = lanceService.findAll();
        List<Leilao> leiloes = leilaoService.findAll();
        mv.addObject("lances", lances);
        mv.addObject("leiloes", leiloes);
        return mv;
    }

    @RequestMapping(value = "/lance/list", method = RequestMethod.POST)
    public ModelAndView listByLeilao(@Valid Long id) {
        //System.out.println(id);

        ModelAndView mv = new ModelAndView("listlances_adm");
        List<Lance> lances = lanceService.findByLeilao(id);
        List<Leilao> leiloes = leilaoService.findAll();
        mv.addObject("lances", lances);
        mv.addObject("leiloes", leiloes);
        return mv;
    }

    @RequestMapping(value = "/lance_list/{id}", method = RequestMethod.GET)
    public ModelAndView listLances_user(@PathVariable("id") long id) {
        ModelAndView mv = new ModelAndView("listlances_user");
        List<Lance> lances = lanceService.findByLeilao(id);
        Leilao leilao = leilaoService.findById(id);

        Collections.sort(lances, new Comparator<Lance>() {
            @Override
            public int compare(Lance o1, Lance o2) {
                Lance l1 = (Lance) o1;
                Lance l2 = (Lance) o2;
                return l2.getValor().compareTo(l1.getValor());
            }
        });

        mv.addObject("lances", lances);
        mv.addObject("leilao", leilao);
        return mv;
    }


    @RequestMapping(value = "/lance_list/{id}", method = RequestMethod.POST)
    public String saveLance(@Valid String valor,
                            @PathVariable("id") long id,
                            Principal principal,
                            RedirectAttributes attributes) {

        Leilao leilao = leilaoService.findById(id);
        Usuario usuario = usuarioService.findByEmail(principal.getName().trim());
        Lance lance = new Lance();
        lance.setLeilao(leilao);
        lance.setUsuario(usuario);
        lance.setValor(new BigDecimal(valor));

        String resultado = lanceService.fazLance(lance);
        //System.out.println(resultado);
        if (resultado.equals("ERROR:01 - lance menor que valor minimo de leilao.")) {
            attributes.addFlashAttribute("messagem", "O lance realizado é menor que o valor mínimo do leilão.");
            return "redirect:/lance_list/{id}";
        } else if (resultado.equals("ERROR:02 - lance nao pode ser menor que o ultimo lance.")) {
            attributes.addFlashAttribute("messagem", "O lance realizado é menor que o valor do ultimo lance.");
            return "redirect:/lance_list/{id}";
        } else if (resultado.equals("ERROR:03 - nao e permitido fazer dois lances seguidos.")) {
            attributes.addFlashAttribute("messagem", "Não é permitido o mesmo usuário fazer dois lances seguidos.");
            return "redirect:/lance_list/{id}";
        }

        return "redirect:/lance_list/{id}";
    }
}