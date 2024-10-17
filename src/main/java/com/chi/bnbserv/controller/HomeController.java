package com.chi.bnbserv.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HomeController {
    @GetMapping("/home")
    public String home() {
        // model.addAttribute("title", "Home Page");
        return "main";
    }

    @GetMapping("/get-data/listing")
    public String downloadListingData() {
        // model.addAttribute("title", "Home Page");
        return "main";
    }
    
}
