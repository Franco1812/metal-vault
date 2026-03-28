package com.spotify.streaming_service;

import com.spotify.streaming_service.model.Cancion;
import com.spotify.streaming_service.repository.CancionRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CancionRepository cancionRepository;

    public DataInitializer(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    @Override
    public void run(String... args) {
        Cancion tornado = new Cancion();
        tornado.setTitulo("Tornado of Souls");
        tornado.setArtista("Megadeth");
        tornado.setAlbum("Rust in Peace");
        tornado.setDuracion(5.19);
        tornado.setUrlArchivo("musica/tornado.mp3");

        Cancion trooper = new Cancion();
        trooper.setTitulo("The Trooper");
        trooper.setArtista("Iron Maiden");
        trooper.setAlbum("Piece of Mind");
        trooper.setDuracion(4.10);
        trooper.setUrlArchivo("musica/trooper.mp3");

        Cancion levitating = new Cancion();
        levitating.setTitulo("Levitating");
        levitating.setArtista("Dua Lipa");
        levitating.setAlbum("Future Nostalgia");
        levitating.setDuracion(3.23);
        levitating.setUrlArchivo("musica/levitating.mp3");

        cancionRepository.saveAll(List.of(tornado, trooper, levitating));
    }
}
