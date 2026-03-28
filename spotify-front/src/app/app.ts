import { Component, signal } from '@angular/core';
import { ListaCancionesComponent } from './components/lista-canciones/lista-canciones';
import { ReproductorComponent } from './components/reproductor/reproductor';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ListaCancionesComponent, ReproductorComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('spotify-front');
}
