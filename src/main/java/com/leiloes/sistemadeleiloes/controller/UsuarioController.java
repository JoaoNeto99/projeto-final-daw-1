package com.leiloes.sistemadeleiloes.controller;

import com.leiloes.sistemadeleiloes.model.Usuario;
import com.leiloes.sistemadeleiloes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(value = "/usuario/list", method = RequestMethod.GET)
    public ModelAndView listAll() {
        ModelAndView mv = new ModelAndView("listusuarios_adm");
        List<Usuario> usuarios = usuarioService.findAll();
        mv.addObject("usuarios", usuarios);
        return mv;
    }

    @RequestMapping(value = "/usuario/new", method = RequestMethod.POST)
    public  String create(@Valid Usuario usuario, BindingResult result,  RedirectAttributes attributes) {

        if(result.hasErrors()){
            attributes.addFlashAttribute("messagem", "Verifique se os campos obrigatórios foram preenchidos.");
            return "redirect:/registrar";
        }

        Usuario usuarioExistente = usuarioService.findByEmail(usuario.getEmail());
        if(usuarioExistente != null) {
            attributes.addFlashAttribute("messagem", "O e-mail já existe.");
            return "redirect:/registrar";
        }

        usuarioService.save(usuario);

        return "redirect:/";
    }
}
