package com.leiloes.sistemadeleiloes.controller;

import com.leiloes.sistemadeleiloes.model.Leilao;
import com.leiloes.sistemadeleiloes.service.LeilaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class LeilaoController {

    @Autowired
    LeilaoService leilaoService;

    @RequestMapping(value = "/leilao/list", method = RequestMethod.GET)
    public ModelAndView getAllLeiloes() {
        ModelAndView mv = new ModelAndView("listleiloes_adm");
        List<Leilao> leiloes = leilaoService.findAll();
        mv.addObject("leiloes", leiloes);
        return mv;
    }

    @RequestMapping(value = "/leilao/list", method = RequestMethod.POST)
    public ModelAndView getLeiloesByStatus(@Valid String status) {
        ModelAndView mv = new ModelAndView("listleiloes_adm");
        List<Leilao> leiloes;
        if(status.equals("TODOS")) {
            leiloes = leilaoService.findAll();
        } else {
            leiloes = leilaoService.findLeilaoByStatus(status);
        }
        mv.addObject("leiloes", leiloes);
        return mv;
    }

    @RequestMapping(value = "/leilao/list/{id}", method = RequestMethod.GET)
    public String deleteLeilao(@PathVariable("id") long id,
                               RedirectAttributes attributes) {
        String result = leilaoService.delete(id);
        if(result.equals("FAIL")) {
            attributes.addFlashAttribute("messagem", "Não é possível excluir um leilao que já tenha recebido lances");
        }
        return "redirect:/leilao/list";
    }

    @RequestMapping(value = "/leilao/new", method = RequestMethod.GET)
    public String getLeilaoForm(Model model) {
        model.addAttribute(new Leilao());
        return "leilaosaveform_adm";
    }

    @RequestMapping(value = "/leilao/open/{id}", method = RequestMethod.GET)
    public String abreLeilao(@PathVariable("id") long id) {
        leilaoService.abreLeilao(id);
        return "redirect:/leilao/list";
    }

    @RequestMapping(value = "/leilao/finaly/{id}", method = RequestMethod.GET)
    public String finalizaLeilao(@PathVariable("id") long id, RedirectAttributes attributes) {
        String result = leilaoService.finalizaLeilao(id);
        if (result.equals("FAIL")) {
            attributes.addFlashAttribute("messagem", "Um leilão só pode ser FINALIZADO a partir do EXPIRADO ou ABERTO.");
            return "redirect:/leilao/list";
        } else if(result.equals("FAIL - SEM LANCE")) {
            attributes.addFlashAttribute("messagem", "Impossivel finalizar um leilão que nunca recebeu um lance.");
            return "redirect:/leilao/list";
        }
        return "redirect:/leilao/list";
    }

    @RequestMapping(value = "/leilao/new", method = RequestMethod.POST)
    public String saveLeilao(@Valid Leilao leilao, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("messagem", "Verifique se os campos obrigatórios foram preenchidos.");
            return "redirect:/leilao/new";
        }

        leilaoService.save(leilao);
        return "redirect:/leilao/list";
    }

    @RequestMapping(value = "/leilao/update/{id}", method = RequestMethod.GET)
    public String getLeilaoDetails(@PathVariable("id") long id, Model model) {
        Leilao leilao = leilaoService.findById(id);
        model.addAttribute(leilao);
        return "leilaoupdateform_adm";
    }

    @RequestMapping(value = "/leilao/update/{id}", method = RequestMethod.POST)
    public String updateLeilao(@PathVariable("id") long id,
                               @Valid Leilao leilao,
                               BindingResult result,
                               RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("messagem", "Verifique se os campos obrigatórios foram preenchidos.");
            return "redirect:/leilao/update/" + id;
        }
        leilaoService.update(leilao);
        return "redirect:/leilao/list";
    }

    /*-----------------------------*/

    @RequestMapping(value = "/leilao_list", method = RequestMethod.GET)
    public ModelAndView listAll_user() {
        ModelAndView mv = new ModelAndView("listleilao_user");
        List<Leilao> leiloes = leilaoService.findLeilaoAbertos();
        mv.addObject("leiloes", leiloes);
        return mv;
    }


}
