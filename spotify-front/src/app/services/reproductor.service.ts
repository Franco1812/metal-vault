import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface ReproductorPayload {
  videoId?: string | null;
  audioUrl?: string | null;
}
@Injectable({
  providedIn: 'root',
})
export class ReproductorService {
  private readonly payloadSubject = new BehaviorSubject<ReproductorPayload | null>(null);
  readonly payload$ = this.payloadSubject.asObservable();

  setVideoId(videoId: string): void {
    this.payloadSubject.next({ videoId });
  }

  setAudioUrl(audioUrl: string): void {
    this.payloadSubject.next({ audioUrl });
  }
}
