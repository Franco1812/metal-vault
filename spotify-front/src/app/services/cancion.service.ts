import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

export interface Cancion {
  id?: number;
  titulo: string;
  artista: string;
  album?: string;
  duracion?: number | string;
  portada?: string;
  url?: string;
  urlArchivo?: string;
  videoId?: string;
  urlPortada?: string;
}

@Injectable({
  providedIn: 'root',
})
export class CancionService {
  private readonly apiUrl = 'http://localhost:8080/api/canciones';

  constructor(private readonly http: HttpClient) {}

  private extractVideoId(value?: string): string | undefined {
    if (!value) {
      return undefined;
    }

    const trimmed = value.trim();
    if (!trimmed) {
      return undefined;
    }

    if (trimmed.length <= 20 && !trimmed.includes('/')) {
      return trimmed;
    }

    const urlMatch = trimmed.match(/(?:v=|vi\/|youtu\.be\/)([A-Za-z0-9_-]{6,})/);
    return urlMatch?.[1];
  }

  getCanciones(query: string): Observable<Cancion[]> {
    return this.http
      .get<Cancion[]>(this.apiUrl, {
        params: { buscar: query },
      })
      .pipe(
        map((canciones) =>
          canciones.map((cancion) => {
            const videoId =
              cancion.videoId ??
              (cancion as { videoID?: string }).videoID ??
              (cancion as { idVideo?: string }).idVideo ??
              this.extractVideoId(cancion.urlPortada) ??
              this.extractVideoId(cancion.portada) ??
              this.extractVideoId(cancion.url);

            return {
              ...cancion,
              url: cancion.url ?? cancion.urlArchivo,
              portada: cancion.portada ?? cancion.urlPortada,
              videoId,
              urlPortada:
                cancion.urlPortada ??
                (videoId
                  ? `https://img.youtube.com/vi/${videoId}/hqdefault.jpg`
                  : undefined),
            };
          }),
        ),
      );
  }
}
