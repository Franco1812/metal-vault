package com.spotify.streaming_service.service;

import com.spotify.streaming_service.model.Cancion;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CancionService {

    private final RestTemplate restTemplate;
    private final YoutubeService youtubeService;

    public CancionService(RestTemplate restTemplate, YoutubeService youtubeService) {
        this.restTemplate = restTemplate;
        this.youtubeService = youtubeService;
    }

    public List<Cancion> buscarCancionesEnDeezer(String query) {
        String url = "https://api.deezer.com/search?q={query}";
        ResponseEntity<DeezerSearchResponse> response =
                restTemplate.getForEntity(url, DeezerSearchResponse.class, query);
        DeezerSearchResponse body = response.getBody();
        if (body == null || body.data == null) {
            return Collections.emptyList();
        }
        List<Cancion> canciones = body.data.stream().map(this::toCancion).collect(Collectors.toList());
        completarConYoutube(canciones);
        return canciones;
    }

    private Cancion toCancion(DeezerTrack track) {
        Cancion cancion = new Cancion();
        cancion.setTitulo(track.title);
        cancion.setArtista(track.artist != null ? track.artist.name : null);
        cancion.setAlbum(track.album != null ? track.album.title : null);
        cancion.setDuracion(track.duration != null ? track.duration / 60.0 : null);
        cancion.setUrlArchivo(track.preview);
        // Por defecto, reproducimos el preview de Deezer.
        cancion.setPlayUrl(track.preview);
        return cancion;
    }

    private void completarConYoutube(List<Cancion> canciones) {
        for (Cancion cancion : canciones) {
            String artista = cancion.getArtista() != null ? cancion.getArtista() : "";
            String titulo = cancion.getTitulo() != null ? cancion.getTitulo() : "";
            String consulta = (artista + " " + titulo).trim();
            if (consulta.isEmpty()) {
                continue;
            }
            YoutubeService.YoutubeVideo video = youtubeService.buscarVideo(consulta);
            if (video != null) {
                cancion.setVideoId(video.getVideoId());
                cancion.setUrlThumbnail(video.getUrlThumbnail());
                if (video.getVideoId() != null && !video.getVideoId().isBlank()) {
                    cancion.setPlayUrl("https://www.youtube.com/watch?v=" + video.getVideoId());
                }
            }
        }
    }

    public static class DeezerSearchResponse {
        public List<DeezerTrack> data;
    }

    public static class DeezerTrack {
        public String title;
        public Integer duration;
        public String preview;
        public DeezerArtist artist;
        public DeezerAlbum album;
    }

    public static class DeezerArtist {
        public String name;
    }

    public static class DeezerAlbum {
        public String title;
    }
}
