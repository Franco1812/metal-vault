import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { map, Observable } from 'rxjs';
import { ReproductorService } from '../../services/reproductor.service';

@Component({
  selector: 'app-reproductor',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reproductor.html',
  styleUrl: './reproductor.css',
})
export class ReproductorComponent {
  private readonly reproductorService = inject(ReproductorService);
  private readonly sanitizer = inject(DomSanitizer);

  readonly iframeSrc$: Observable<SafeResourceUrl | null> =
    this.reproductorService.payload$.pipe(
      map((payload) => {
        if (!payload?.videoId) {
          return null;
        }
        return this.sanitizer.bypassSecurityTrustResourceUrl(
          `https://www.youtube.com/embed/${payload.videoId}?autoplay=1`,
        );
      }),
    );

  readonly audioUrl$: Observable<string | null> = this.reproductorService.payload$.pipe(
    map((payload) => payload?.audioUrl ?? null),
  );
}
