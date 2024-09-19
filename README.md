# RecruitRing - System Śledzenia Kandydatów

**RecruitRing** to system śledzenia kandydatów (ATS), stworzony w celu usprawnienia i zarządzania procesem rekrutacyjnym w działach HR. Projekt ten jest realizowany w ramach pracy inżynierskiej.

## Opis projektu
RecruitRing ma na celu ułatwienie procesu rekrutacji poprzez automatyzację zarządzania ofertami pracy, kandydatami oraz rekruterami. Aplikacja pozwala rekruterom na śledzenie statusu kandydatów, planowanie rozmów kwalifikacyjnych oraz wysyłanie powiadomień e-mailowych.

## Funkcjonalności
- Zabezpieczony system logowania (JWT).
- Zarządzanie użytkownikami (role: administrator, rekruter).
- Tworzenie i zarządzanie ofertami pracy.
- Publiczna strona z listą ofert pracy dla kandydatów.
- Obsługa aplikacji kandydatów, zmiana statusów (np. "Rozmowa kwalifikacyjna").
- Automatyczne wysyłanie powiadomień e-mail do kandydatów.
- Panel administracyjny do zarządzania rekrutacjami.

## Technologie
Projekt jest realizowany z wykorzystaniem następujących technologii:
- **Backend**: Java (Spring Boot)
- **Frontend**: Angular
- **Baza danych**: PostgreSQL
- **Inne**: JWT (zabezpieczenie), OpenShift (deployment), Lombok, SonarQube

## Instalacja i konfiguracja
Aby uruchomić projekt lokalnie, wykonaj poniższe kroki:

1. Sklonuj repozytorium:
    ```bash
    git clone https://github.com/radziu2402/RecruitRing-ATS.git
    ```

2. Skonfiguruj bazę danych (PostgreSQL) i ustaw dane dostępowe w pliku `application.properties`.

3. Uruchom backend:
    ```bash
    cd recruitring-core
    mvnw spring-boot:run
    ```

4. Przejdź do folderu Angulara i uruchom frontend:
    ```bash
    cd angular-code
    npm install
    ng serve
    ```

5. Aplikacja powinna być dostępna pod adresem: `http://localhost:4200`.
