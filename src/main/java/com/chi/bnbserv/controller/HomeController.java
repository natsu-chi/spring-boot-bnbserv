package com.chi.bnbserv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("path", "fragments/home :: home");
        model.addAttribute("pathSrc", "");
        model.addAttribute("withSrc", "N");
        return "main";
    }
    
}
