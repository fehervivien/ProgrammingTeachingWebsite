package com.ptw.progteaching.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {

    // Kezdőlap
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Kurzusról oldal
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    // Profil oldal
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    // Kurzus oldal betöltése
    @GetMapping("/course")
    public String course() {
        return "course";
    }
}
