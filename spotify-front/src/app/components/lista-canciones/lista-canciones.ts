import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Cancion, CancionService } from '../../services/cancion.service';
import { ReproductorService } from '../../services/reproductor.service';

@Component({
  selector: 'app-lista-canciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lista-canciones.html',
  styleUrl: './lista-canciones.css',
})
export class ListaCancionesComponent implements OnInit {
  canciones: Cancion[] = [];
  cargando = true;
  error: string | null = null;
  terminoBusqueda = 'Megadeth';
  artistaActual = 'Megadeth';
  readonly placeholderPortada =
    "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='48' height='48'><rect width='100%25' height='100%25' fill='%2322262b'/><text x='50%25' y='50%25' fill='%23949aa3' font-size='10' text-anchor='middle' dominant-baseline='middle'>No Art</text></svg>";

  constructor(
    private readonly cancionService: CancionService,
    private readonly reproductorService: ReproductorService,
  ) {}

  ngOnInit(): void {
    this.buscarCon(this.terminoBusqueda);
  }

  buscar(): void {
    const termino = this.terminoBusqueda.trim();
    if (!termino) {
      this.error = 'Ingresá una banda para buscar.';
      return;
    }
    this.buscarCon(termino);
  }

  buscarCon(termino: string): void {
    this.canciones = [];
    this.cargando = true;
    this.error = null;
    this.terminoBusqueda = termino;
    this.artistaActual = termino;
    this.cancionService.getCanciones(termino).subscribe({
      next: (data) => {
        this.canciones = data;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las canciones.';
        this.cargando = false;
      },
    });
  }

  play(cancion: Cancion): void {
    if (cancion.videoId) {
      this.reproductorService.setVideoId(cancion.videoId);
      return;
    }
    if (cancion.urlArchivo || cancion.url) {
      this.reproductorService.setAudioUrl(cancion.urlArchivo ?? cancion.url ?? '');
      return;
    }

    this.error = 'Este tema no tiene una URL reproducible.';
  }

  onImgError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src = this.placeholderPortada;
  }

  onImgErrorForCancion(event: Event, cancion: Cancion): void {
    const img = event.target as HTMLImageElement;
    if (cancion.videoId && !img.src.includes(cancion.videoId)) {
      img.src = `https://img.youtube.com/vi/${cancion.videoId}/hqdefault.jpg`;
      return;
    }
    img.src = this.placeholderPortada;
  }
}
