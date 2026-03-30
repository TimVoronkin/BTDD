# <img src="src/main/resources/static/imgs/icon.png" width="48" height="48" alt="icon" style="vertical-align: middle; padding-right: 10px;"/> CinemaBooking - Dokumentace
Projekt Tima Voronkina pro předmět Programování řízené testy (BTDD).

## Popis domény a funkcí
**CinemaBooking** je webová aplikace na bázi Spring Boot pro správu rezervací vstupenek v kině. Umožňuje uživatelům prohlížet dostupné filmy a projekce a také rychle a pohodlně rezervovat vstupenky online.
Hlavní funkce systému:
- Prohlížení programu filmů s metadaty (získávána přes externí API služby, např. OMDB).
- Rezervace vstupenek s dodržením přísných obchodních pravidel (kontrola dostupnosti volných míst, omezení nákupu na maximálně 6 vstupenek na osobu, kontrola věkového hodnocení filmu).
- Automatický výpočet ceny: ochrana proti záporné ceně a uplatnění automatické slevy 20 % na dopolední projekce (do 12:00).
- Zrušení rezervací (povoleno nejpozději 1 hodinu před začátkem projekce).

## Návod ke spuštění
Aplikaci lze spustit lokálně několika způsoby:

1. **Pomocí Dockeru:**<br>
   Projekt obsahuje multi-stage Dockerfile, který automaticky zkompiluje a spustí aplikaci bez nutnosti instalovat lokálně Java prostředí. Z kořenové složky spusťte:
   ```bash
   docker build -t cinema-booking .
   docker run -p 8080:8080 cinema-booking
   ```
   Aplikace bude následně dostupná na adrese: [http://localhost:8080](http://localhost:8080).

2. **Spuštění přes terminál (Maven):**
   ```bash
   mvn spring-boot:run
   ```
   *Poznámka: Testovací data (filmy a rozvrh) se u obou způsobů automaticky vygenerují do in-memory databáze H2 během startu.*

3. **Spuštění testů a vygenerování zprávy o pokrytí kódů:**
   ```bash
   mvn clean test jacoco:report
   ```
   Zprávu s analýzou v JaCoCo naleznete v souboru `target/site/jacoco/index.html`.

## Architektura systému
Projekt je založen na klasické vícevrstvé architektuře (Layered Architecture):
- **Controllers (Prezentační vrstva):** Zpracovávají HTTP požadavky frontendových formulářů a komunikují s uživatelem přes HTML šablony (Thymeleaf). Neobsahují zásadní obchodní logiku.
- **Service (Vrstva obchodní logiky):** Srdce projektu. Zde jsou implementována veškerá doménová pravidla rezervací (`BookingService`) a adaptéry pro komunikaci s externími API (`OmdbService`).
- **Repository (Datová vrstva):** Poskytuje rozhraní Spring Data JPA pro interakci s databází H2.
- **Domain (Vrstva entit):** Hlavní datové modely reprezentující tabulky (`Movie`, `Screening`, `Booking`).

## Strategie testování
Vývoj probíhal ověřenou metodikou **TDD (Test-Driven Development)**:
- **Jednotkové (Unit) testy:** Ověřují menší izolované komponenty (algoritmy slev, storno podmínky). Jsou bleskurychlé a spouštějí se bez startování databáze.
- **Integrační testy:** Zjišťují, zda spolu vrstvy správně komunikují v reálném nasazení (např. zápis entity z vrstvy Service přímo do in-memory databáze).
- **BDD (Behavior-Driven Development) testy:** Ověřují celkové případové užití. Testy mají strukturu `Given - When - Then` (Dáno - Když - Pak), a tak fungují jako srozumitelná technická dokumentace k logickým scénářům uživatelů.

**Využití objektů Mock:**
Při jednotkovém testování (`BookingServiceTest`) byly používány záměrné závislosti typu **Mock** (náhražky) pro `BookingRepository`, `ScreeningRepository` i pro službu `OmdbService`.
*Důvody k použití:*
1. **Rychlost:** K ověření výpočtu matematické slevy není potřeba navazovat pomalé fyzické spojení se souborem databáze.
2. **Determinismus a izolace:** Umožňuje předem nasimulovat chybové zprávy z externích API (třeba simulace odpojení od sítě), číst přesná data beze změny testovací DB (testování filmu s přesně 0 volnými místy), aby nedocházelo k náhodnému selhání validních testů.

## Pokrytí kódu (Code Coverage)
JaCoCo plugin v projektu vyhodnocuje metriky otestovaného kódu, které jsou primárně mířeny na nejdůležitější vrstvy logiky:
- **Stanovené cíle:** $\ge$ **80 % pokrytí řádků (Line Coverage)** a $\ge$ **70 % pokrytí větvení (Branch Coverage).** Účtuje se, že nejdůležitější bloky `if/else`, try/catch a výpočtové algoritmy jsou tak spolehlivě otestovány proti chybám.

**Výjimky (kód neotestovaný záměrně):**
- **Vnitřnosti DTO/POJO a Controllery:** Obyčejné konstruktory, `@Getter` a `@Setter` metody představují automaticky vygenerovaný nebo zcela stereotypní kód (bez logiky nebo skrytých cyklů). Controllery fungují jen jako předávací mechanismus. Náklady na vytváření umělého MockMvc prostředí pro každou takovou metodu jsou v nepoměru s reálnou přidanou hodnotou (nízké ROI).
- **Bootovací a konfigurační třídy (`DataInitializer`, Startovací main metoda):** Třídy spouštějící Spring Boot se validují komplexními integračními (E2E) spuštěními. Modulární mockování těchto vrstev je nepraktické a zbytečné z hlediska kvality domény projektu.
