# URL Shortener

Prosty i bezpieczny serwis do skracania linków.

## Funkcjonalności

- Rejestracja i logowanie użytkowników (JWT)
- Dostęp do swoich linków tylko po zalogowaniu
- Skracanie linków z możliwością podania własnego aliasu (`customTag`)
- Zliczanie kliknięć w linki przez użytkowników
- Przekierowanie do oryginalnego adresu po wejściu w skrócony URL

## Technologie

- Java 21
- Spring Boot 3.5.4
- Spring Security + JWT
- Spring Web
- Spring Data JPA (H2 Database)
- OpenAPI 3 (springdoc-openapi-ui)
- JUnit
- Docker
- Maven
- Make

## API – Endpoints [Pobierz api-docs-postman.json](api-docs-postman.json)

### Autoryzacja

#### `POST /api/auth/register`

Rejestracja użytkownika.

**Request Body:**

```json
{
  "email": "user@user.com",
  "password": "passw0rd123"
}
```

#### `POST /api/auth/login`

Logowanie użytkownika. (Wraz ze startem aplikacji tworzony jest testowy użytkownik o danych: "admin@admin.pl", "pass")

**Request Body:**

```json
{
  "email": "aaa@gmail.com",
  "password": "password"
}
```

**Response:**

```json
{
  "code": 200,
  "token": "jwt-token"
}
```

### Skracanie linków

#### `POST /api/shorten`

Tworzy skrócony link. Endpoint nie jest zabezpieczony, lecz po podaniu JWT (nagłówek `Authorization: Bearer <jwt>`) automatycznie skrócony link jest przypisywany do usera.

**Request Body:**

```json
{
  "url": "https://google.com",
  "customTag": "tag123"
}
```

**Response:**

```json
{
  "code": 200,
  "url": "http://localhost:8080/tag123"
}
```

### Przekierowanie

#### `GET /{tag}`

Wejście w skrócony link – redirect na docelowy URL.

### Linki Użytkownika

#### `GET /api/my-links`

Zwraca listę skróconych linków użytkownika (zlicza kliknięcia + daty utworzenia).

**Nagłówek:**

```
Authorization: Bearer <jwt>
```

**Response:**

```json
[
  {
    "originalUrl": "https://google.com",
    "shortenedUrl": "http://localhost:8080/tag123",
    "clicks": 12,
    "createdAt": "2025-08-06T12:12:12"
  }
]
```

## Szybki start

### Uruchomienie aplikacji

W pliku `.env` ustaw zmienną JWT_SECRET (jak w przykładowym [.env.example](.env.example)).

**Zbuduj i uruchom kontener:**

```bash
make up
```

Aplikacja dostępna pod: [http://localhost:8080](http://localhost:8080)

### Testy lokalne

```bash
make test-local
```

### Zatrzymanie kontenera:

```bash
make stop
```

## Dokumentacja OpenAPI

Po uruchomieniu dostępna pod:

```
http://localhost:8080/swagger-ui.html
```

## ️ Struktura projektu

- `controller` – warstwa REST
- `service` – logika biznesowa
- `repository` – interakcja z bazą danych (JPA)
- `entity` – modele danych
- `config` – konfiguracja aplikacji
- `exception` – customowa obsługa wyjątków
- `utils` - pliki dodatkowe (np: DTO, Tag Generator)
- `test` - Testy jednostkowe logiki biznesowej aplikacji
