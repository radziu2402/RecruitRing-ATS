# RecruitRing ATS

## Opis projektu

**RecruitRing ATS** to aplikacja typu ATS (Applicant Tracking System) stworzona w ramach pracy inżynierskiej przez Radosława Zimocha w semestrze zimowym 2024/2025. Projekt jest podzielony na dwa główne moduły:

- **Frontend (Angular)** – kod znajduje się w folderze `angular-code`.
- **Backend (Spring Boot)** – kod znajduje się w folderze `recruitring-core`.

Aplikacja umożliwia zarządzanie ofertami pracy, aplikacjami kandydatów, kontami użytkowników oraz kalendarzem wydarzeń. Wszystkie komponenty są konteneryzowane, co pozwala na łatwe uruchomienie za pomocą Dockera.

## Uruchomienie aplikacji

### Wymagania wstępne

- **Docker** oraz **Docker Compose** muszą być zainstalowane na Twoim komputerze.

### Instrukcja uruchomienia

1. **Pobierz projekt**  
   Sklonuj repozytorium z kodem aplikacji:
   ```bash
   git clone https://github.com/radziu2402/RecruitRing-ATS
   cd RecruitRing-ATS
   ```

2. **Uruchom aplikację**  
   W katalogu z plikiem `docker-compose.yml` wykonaj następujące polecenie:
   ```bash
   docker-compose up --build
   ```
   To polecenie zbuduje obrazy Dockera oraz uruchomi następujące usługi:
   - **Baza danych PostgreSQL**
   - **Backend (Spring Boot)** – dostępny na porcie `8443`
   - **Frontend (Angular)** – dostępny na porcie `4200`

3. **Uzyskaj dostęp do aplikacji**  
   Po zakończeniu budowy obrazów, aplikacja będzie dostępna pod adresami:
   - **Frontend**: [https://localhost:4200](https://localhost:4200)
   - **Backend**: [https://localhost:8443](https://localhost:8443)

4. **Zatrzymanie aplikacji**  
   Aby zatrzymać wszystkie usługi, użyj:
   ```bash
   docker-compose down
   ```

## Testowanie aplikacji

Projekt zawiera plik `data.sql`, który inicjalizuje bazę danych przykładowymi danymi. Aby zalogować się do aplikacji, użyj następujących danych:

- **Nazwa użytkownika**: `admin`
- **Hasło**: `password`

Po zalogowaniu możesz testować funkcje aplikacji, takie jak przeglądanie ofert pracy, zarządzanie użytkownikami i aplikacjami kandydatów.

## Licencja

Projekt jest udostępniany na licencji MIT. Oznacza to, że:

- Możesz kopiować, modyfikować i dystrybuować kod projektu w celach prywatnych i komercyjnych.
- Musisz zachować informacje o licencji w sklonowanych kopiach projektu.

Pełna treść licencji znajduje się w pliku `LICENSE` dołączonym do projektu.
