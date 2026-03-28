package com.spotify.streaming_service.controller;

import com.spotify.streaming_service.model.Cancion;
import com.spotify.streaming_service.service.CancionService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/canciones")
public class CancionController {

    private final CancionService cancionService;

    public CancionController(CancionService cancionService) {
        this.cancionService = cancionService;
    }

    @GetMapping
    public List<Cancion> buscar(@RequestParam("buscar") String query) {
        return cancionService.buscarCancionesEnDeezer(query);
    }
}
