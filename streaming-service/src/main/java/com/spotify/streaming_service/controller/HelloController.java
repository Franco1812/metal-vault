package com.spotify.streaming_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hola")
    public String saludar() {
        return "¡Bienvenido a Spotify! El servidor está re en esa. 🎸";
    }
}
