package com.leiloes.sistemadeleiloes.controller;

import com.leiloes.sistemadeleiloes.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getHome(){
        return new ModelAndView("home");
    }

    @RequestMapping(value = "/registrar", method = RequestMethod.GET)
    public ModelAndView registrar(Model model){
        model.addAttribute(new Usuario());
        return new ModelAndView("registre-se");
    }
}
