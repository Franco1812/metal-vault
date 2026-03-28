package com.spotify.streaming_service.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class YoutubeService {

    private static final String APPLICATION_NAME = "streaming-service";
    private static final Logger logger = LoggerFactory.getLogger(YoutubeService.class);

    private final String apiKey;

    public YoutubeService(@Value("${youtube.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public YoutubeVideo buscarVideo(String consulta) {
        if (apiKey == null || apiKey.isBlank() || consulta == null || consulta.isBlank()) {
            if (apiKey == null || apiKey.isBlank()) {
                logger.warn("youtube.api.key no configurada; no se consulta YouTube.");
            }
            return null;
        }
        try {
            YouTube youtube = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    request -> {})
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            YouTube.Search.List search = youtube.search().list(List.of("id", "snippet"));
            search.setKey(apiKey);
            search.setQ(consulta);
            search.setType(List.of("video"));
            search.setMaxResults(1L);

            SearchListResponse response = search.execute();
            if (response.getItems() == null || response.getItems().isEmpty()) {
                logger.info("YouTube sin resultados para consulta: {}", consulta);
                return null;
            }

            SearchResult item = response.getItems().get(0);
            String videoId = item.getId() != null ? item.getId().getVideoId() : null;
            String urlThumbnail = null;
            if (item.getSnippet() != null && item.getSnippet().getThumbnails() != null) {
                if (item.getSnippet().getThumbnails().getHigh() != null) {
                    urlThumbnail = item.getSnippet().getThumbnails().getHigh().getUrl();
                } else if (item.getSnippet().getThumbnails().getMedium() != null) {
                    urlThumbnail = item.getSnippet().getThumbnails().getMedium().getUrl();
                } else if (item.getSnippet().getThumbnails().getDefault() != null) {
                    urlThumbnail = item.getSnippet().getThumbnails().getDefault().getUrl();
                }
            }

            return new YoutubeVideo(videoId, urlThumbnail);
        } catch (IOException | GeneralSecurityException e) {
            logger.warn("Error consultando YouTube: {}", e.getMessage());
            return null;
        }
    }

    public static class YoutubeVideo {
        private final String videoId;
        private final String urlThumbnail;

        public YoutubeVideo(String videoId, String urlThumbnail) {
            this.videoId = videoId;
            this.urlThumbnail = urlThumbnail;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getUrlThumbnail() {
            return urlThumbnail;
        }
    }
}
