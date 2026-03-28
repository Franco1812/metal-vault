# Metal Vault

Buscador de canciones orientado al metal con frontend en Angular y backend en Spring Boot.
El backend consulta la API publica de Deezer y, si esta configurada, enriquece los resultados con datos de YouTube.

**Estructura**
- `spotify-front/` Frontend Angular.
- `streaming-service/` API REST Spring Boot.

**Stack**
- Angular 20
- Spring Boot 4
- Java 21
- H2 (runtime)

**Requisitos**
- Node.js 20+ (recomendado para Angular 20).
- Java 21+
- (Opcional) API key de YouTube para thumbnails y play URL.

**Inicio rapido**
1. Backend (Spring Boot)
```powershell
cd streaming-service
.\mvnw.cmd spring-boot:run
```

2. Frontend (Angular)
```powershell
cd spotify-front
npm install
npm start
```

Luego abrí `http://localhost:4200`. La API corre en `http://localhost:8080`.

**Variables de entorno**
- `YOUTUBE_API_KEY` (opcional)

Ejemplo en PowerShell:
```powershell
$env:YOUTUBE_API_KEY="TU_API_KEY"
```

**Endpoints principales**
- `GET /hola`
- `GET /api/canciones?buscar=Metallica`

Respuesta tipica (campos relevantes):
- `titulo`, `artista`, `album`, `duracion`
- `urlArchivo` (preview de Deezer)
- `playUrl` (YouTube si hay API key, si no preview)
- `videoId`, `urlThumbnail`

**Scripts utiles**
Frontend:
- `npm start`
- `npm test`
- `npm run build`

Backend:
- `.\mvnw.cmd test`
