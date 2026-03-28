package com.spotify.streaming_service.repository;

import com.spotify.streaming_service.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancionRepository extends JpaRepository<Cancion, Long> {
}
