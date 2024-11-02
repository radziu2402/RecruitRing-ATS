CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION generate_offer_code()
    RETURNS TRIGGER AS '
BEGIN
    NEW.offer_code := uuid_generate_v4();
    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER set_offer_code
    BEFORE INSERT ON job_postings
    FOR EACH ROW
EXECUTE FUNCTION generate_offer_code();

CREATE OR REPLACE FUNCTION generate_application_code()
    RETURNS TRIGGER AS '
    BEGIN
        NEW.application_code := uuid_generate_v4();
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER set_application_code
    BEFORE INSERT ON applications
    FOR EACH ROW
EXECUTE FUNCTION generate_application_code();

INSERT INTO users (login, email, password, role)
VALUES ('admin', 'admin@example.com', '$2a$10$paWcoAGkFwu.rAeNNcfPv.FfRelOjwSuYw/iacp3HCbLpkuJN86iO', 'ADMINISTRATOR');

INSERT INTO users (login, email, password, role)
VALUES ('user', 'user@example.com', '$2a$10$paWcoAGkFwu.rAeNNcfPv.FfRelOjwSuYw/iacp3HCbLpkuJN86iO', 'RECRUITER');

INSERT INTO recruiters (first_name, last_name, position, date_of_birth, user_id)
VALUES ('Admin', 'Example', 'HR Manager', '1980-01-01',
        (SELECT id FROM users WHERE login = 'admin'));

INSERT INTO recruiters (first_name, last_name, position, date_of_birth, user_id)
VALUES ('User', 'Example', 'Recruiter', '1990-01-01',
        (SELECT id FROM users WHERE login = 'user'));

INSERT INTO events (title, description, start_time, end_time, recruiter_id)
VALUES
    ('Spotkanie z kandydatem A', 'Rozmowa kwalifikacyjna na stanowisko programisty.',
     '2024-11-03 10:00:00', '2024-11-03 11:00:00',
     (SELECT id FROM recruiters WHERE user_id = (SELECT id FROM users WHERE login = 'user'))),

    ('Warsztat DevOps', 'Szkolenie dotyczące praktyk CI/CD i automatyzacji wdrożeń.',
     '2024-11-05 19:00:00', '2024-11-05 22:00:00',
     (SELECT id FROM recruiters WHERE user_id = (SELECT id FROM users WHERE login = 'user'))),

    ('Spotkanie z zespołem', 'Omówienie bieżących rekrutacji oraz postępów projektowych.',
     '2024-11-06 14:00:00', '2024-11-06 15:30:00',
     (SELECT id FROM recruiters WHERE user_id = (SELECT id FROM users WHERE login = 'user'))),

    ('Przegląd aplikacji', 'Weryfikacja i ocena aplikacji kandydatów.',
     '2024-11-07 05:00:00', '2024-11-07 06:00:00',
     (SELECT id FROM recruiters WHERE user_id = (SELECT id FROM users WHERE login = 'user'))),

    ('Spotkanie HR', 'Dyskusja na temat strategii rekrutacyjnych na przyszły rok.',
     '2024-11-10 15:00:00', '2024-11-10 16:30:00',
     (SELECT id FROM recruiters WHERE user_id = (SELECT id FROM users WHERE login = 'admin')));


-- Nowi użytkownicy z rolą RECRUITER
INSERT INTO users (login, email, password, role)
VALUES ('recruiter1', 'recruiter1@example.com', '$2a$10$paWcoAGkFwu.rAeNNcfPv.FfRelOjwSuYw/iacp3HCbLpkuJN86iO', 'RECRUITER');

INSERT INTO users (login, email, password, role)
VALUES ('recruiter2', 'recruiter2@example.com', '$2a$10$paWcoAGkFwu.rAeNNcfPv.FfRelOjwSuYw/iacp3HCbLpkuJN86iO', 'RECRUITER');

INSERT INTO users (login, email, password, role)
VALUES ('recruiter3', 'recruiter3@example.com', '$2a$10$paWcoAGkFwu.rAeNNcfPv.FfRelOjwSuYw/iacp3HCbLpkuJN86iO', 'RECRUITER');

-- Przypisanie danych osobowych nowym rekruterom
INSERT INTO recruiters (first_name, last_name, position, date_of_birth, user_id)
VALUES ('John', 'Doe', 'Recruiter', '1985-05-20',
        (SELECT id FROM users WHERE login = 'recruiter1'));

INSERT INTO recruiters (first_name, last_name, position, date_of_birth, user_id)
VALUES ('Anna', 'Smith', 'Recruiter', '1992-08-15',
        (SELECT id FROM users WHERE login = 'recruiter2'));

INSERT INTO recruiters (first_name, last_name, position, date_of_birth, user_id)
VALUES ('Peter', 'Johnson', 'Recruiter', '1988-03-10',
        (SELECT id FROM users WHERE login = 'recruiter3'));



-- Wprowadź dane do tabeli Title
INSERT INTO titles (name) VALUES
                              ('Vice President - Remarketing'),
                              ('Senior Innovation Manager'),
                              ('Senior Software Node Owner, Electromobility'),
                              ('Java Software Engineer (Senior)'),
                              ('Executive Assistant'),
                              ('Team Leader - Sales'),
                              ('Production Manager'),
                              ('Senior Innovation Engineer'),
                              ('Data Scientist - Machine Learning'),
                              ('Marketing Director - Europe'),
                              ('Product Manager - Industrial Products'),
                              ('Lead Data Scientist - Artificial Intelligence'),
                              ('Junior Sales Manager - Automotive'),
                              ('IT Security Specialist'),
                              ('UX/UI Designer - Digital Products'),
                              ('Financial Analyst'),
                              ('Software Architect - Cloud Solutions'),
                              ('Business Development Manager - International Markets'),
                              ('Customer Support Specialist - IT'),
                              ('Senior Legal Counsel - Corporate Affairs');

-- Wprowadź dane do tabeli Location
INSERT INTO locations (name) VALUES
                                 ('Wacol, QLD, AU, 4076'),
                                 ('Eskilstuna, SE, 405 08'),
                                 ('Göteborg, SE, 417 15'),
                                 ('Wroclaw, PL, 51-502'),
                                 ('Göteborg, SE, 417 56'),
                                 ('Beaufort West, ZA, 6970'),
                                 ('Dublin, VA, US, 24084'),
                                 ('Bangalore, IN, 562122'),
                                 ('Milano, IT, 20151');

-- Wprowadź dane do tabeli JobCategory
INSERT INTO job_categories (name) VALUES
                                      ('Sprzedaż i usługi'),
                                      ('Planowanie produktów i technologii'),
                                      ('Inżynieria technologii'),
                                      ('Technologia informacyjna'),
                                      ('Wsparcie administracyjne i biznesowe'),
                                      ('Produkcja'),
                                      ('Projektowanie produktu'),
                                      ('Finanse'),
                                      ('Wsparcie techniczne'),
                                      ('Prawo i administracja'),
                                      ('Marketing');






-- Zaktualizuj job_postings
INSERT INTO job_postings (title_id, description, location_id, work_type, created_at, job_category_id)
VALUES
    ((SELECT id FROM titles WHERE name = 'Vice President - Remarketing'),
     'Jako Vice President ds. Remarketingu, będziesz odpowiedzialny za rozwijanie i realizację globalnych strategii remarketingowych w ramach naszej organizacji. Twoje zadania będą obejmować nie tylko bieżącą analizę rynków, ale także ścisłą współpracę z zespołami marketingu, sprzedaży oraz rozwoju produktu, aby zapewnić spójność w komunikacji i strategii remarketingowej na każdym poziomie. W tej roli będziesz liderem zespołu, który pracuje nad optymalizacją kampanii remarketingowych na skalę globalną, z uwzględnieniem najnowszych trendów i technologii, takich jak automatyzacja procesów marketingowych, personalizacja komunikatów oraz analityka predykcyjna. Będziesz również monitorować i analizować wyniki kampanii, raportując bezpośrednio do zarządu, oraz wprowadzać rekomendacje mające na celu maksymalizację zysków z działań remarketingowych. Oczekujemy, że będziesz współpracować z partnerami zewnętrznymi i agencjami w celu realizacji kampanii o zasięgu międzynarodowym. Idealny kandydat na to stanowisko posiada szeroką wiedzę z zakresu digital marketingu, silne umiejętności analityczne oraz doświadczenie w pracy na stanowiskach kierowniczych w międzynarodowych firmach. Korzyści płynące z pracy na tym stanowisku obejmują konkurencyjne wynagrodzenie, pakiet benefitów, w tym prywatną opiekę zdrowotną oraz możliwość udziału w programach szkoleniowych o zasięgu globalnym.',
     (SELECT id FROM locations WHERE name = 'Wacol, QLD, AU, 4076'),
     'STATIONARY', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Sprzedaż i usługi')),

    ((SELECT id FROM titles WHERE name = 'Senior Innovation Manager'),
     'Jako Senior Innovation Manager będziesz kluczową osobą odpowiedzialną za identyfikację i wprowadzanie innowacyjnych rozwiązań w naszych produktach i usługach. Będziesz pracować nad rozwojem strategii innowacji, współpracując z zespołami badawczo-rozwojowymi, marketingowymi oraz sprzedażowymi. Twoim zadaniem będzie poszukiwanie nowych technologii oraz metod, które mogą usprawnić nasze procesy produkcyjne, zwiększyć wartość produktów dla klientów oraz poprawić efektywność operacyjną. Będziesz również odpowiadać za monitorowanie trendów branżowych oraz analizę konkurencji, aby upewnić się, że nasza firma zawsze pozostaje w czołówce innowacji. Współpracując z partnerami zewnętrznymi, startupami oraz instytucjami badawczymi, będziesz wdrażać projekty pilotażowe oraz testować nowe technologie przed ich wdrożeniem na szeroką skalę. Szukamy osoby z doświadczeniem w prowadzeniu projektów innowacyjnych, doskonałymi umiejętnościami komunikacyjnymi oraz zdolnościami analitycznymi. Praca na tym stanowisku oferuje wyjątkowe możliwości rozwoju kariery, a także konkurencyjne wynagrodzenie oraz elastyczne godziny pracy, z opcją pracy zdalnej.',
     (SELECT id FROM locations WHERE name = 'Eskilstuna, SE, 405 08'),
     'REMOTE', '2024-01-21',
     (SELECT id FROM job_categories WHERE name = 'Planowanie produktów i technologii')),

    ((SELECT id FROM titles WHERE name = 'Senior Software Node Owner, Electromobility'),
     'W tej roli jako Senior Software Node Owner będziesz odpowiedzialny za pełen cykl życia oprogramowania, z naciskiem na aplikacje w sektorze elektromobilności. Twoim zadaniem będzie kierowanie zespołem inżynierów oprogramowania, którzy rozwijają i utrzymują oprogramowanie związane z technologiami elektromobilności, w tym systemami zarządzania energią, optymalizacją zużycia baterii, jak również oprogramowaniem sterującym dla pojazdów elektrycznych. Będziesz odpowiadać za architekturę technologiczną, optymalizację wydajności oraz bezpieczeństwo systemów. Ważnym elementem twojej pracy będzie bliska współpraca z zespołami hardware oraz mechaniki, aby zapewnić integrację oprogramowania z komponentami pojazdów elektrycznych. Twoje zadania obejmą także zapewnienie zgodności z regulacjami międzynarodowymi dotyczącymi bezpieczeństwa oraz wydajności pojazdów elektrycznych. Wymagamy doświadczenia w pracy z systemami embedded, znajomości języków programowania C/C++ oraz umiejętności zarządzania złożonymi projektami technologicznymi. Praca na tym stanowisku oferuje wyjątkowe wyzwania technologiczne, możliwości współpracy z międzynarodowymi zespołami oraz dostęp do nowoczesnych technologii z zakresu elektromobilności.',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 15'),
     'HYBRID', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Inżynieria technologii')),

    ((SELECT id FROM titles WHERE name = 'Java Software Engineer (Senior)'),
     'Jako Senior Java Software Engineer będziesz odpowiedzialny za projektowanie i rozwój systemów rozproszonych oraz aplikacji back-endowych w Java...',
     (SELECT id FROM locations WHERE name = 'Wroclaw, PL, 51-502'),
     'REMOTE', '2024-09-11',
     (SELECT id FROM job_categories WHERE name = 'Technologia informacyjna')),

    -- Dalsze wstawianie
    ((SELECT id FROM titles WHERE name = 'Executive Assistant'),
     'Executive Assistant wspiera codzienne operacje zarządu, dbając o koordynację spotkań, zarządzanie kalendarzem oraz organizację podróży służbowych...',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 56'),
     'STATIONARY', '2024-09-22',
     (SELECT id FROM job_categories WHERE name = 'Wsparcie administracyjne i biznesowe')),

    -- Dodaj resztę pozycji analogicznie, używając odpowiednich ID z tytułów, lokalizacji, i kategorii stanowiska
    ((SELECT id FROM titles WHERE name = 'Team Leader - Sales'),
     'Jako Team Leader w dziale sprzedaży, będziesz odpowiedzialny za prowadzenie zespołu sprzedawców oraz monitorowanie i optymalizację wyników sprzedażowych...',
     (SELECT id FROM locations WHERE name = 'Beaufort West, ZA, 6970'),
     'STATIONARY', '2024-11-23',
     (SELECT id FROM job_categories WHERE name = 'Sprzedaż i usługi')),

    ((SELECT id FROM titles WHERE name = 'Production Manager'),
     'Jako Production Manager, będziesz zarządzać zespołem produkcyjnym w zakładzie wytwarzającym komponenty dla przemysłu motoryzacyjnego...',
     (SELECT id FROM locations WHERE name = 'Dublin, VA, US, 24084'),
     'HYBRID', '2024-01-23',
     (SELECT id FROM job_categories WHERE name = 'Produkcja')),

    ((SELECT id FROM titles WHERE name = 'Senior Innovation Engineer'),
     'W roli Senior Innovation Engineer będziesz pracować nad nowatorskimi projektami technologicznymi...',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 15'),
     'REMOTE', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Inżynieria technologii')),

    ((SELECT id FROM titles WHERE name = 'Data Scientist - Machine Learning'),
     'W tej roli będziesz odpowiedzialny za analizowanie dużych zbiorów danych i rozwijanie algorytmów uczenia maszynowego...',
     (SELECT id FROM locations WHERE name = 'Bangalore, IN, 562122'),
     'HYBRID', '2024-04-23',
     (SELECT id FROM job_categories WHERE name = 'Technologia informacyjna')),

    ((SELECT id FROM titles WHERE name = 'Marketing Director - Europe'),
     'Marketing Director będzie odpowiedzialny za rozwój strategii marketingowej firmy na rynkach europejskich...',
     (SELECT id FROM locations WHERE name = 'Milano, IT, 20151'),
     'REMOTE', '2024-09-11',
     (SELECT id FROM job_categories WHERE name = 'Marketing'));

-- Dodaj więcej ofert pracy do tabeli job_postings
INSERT INTO job_postings (title_id, description, location_id, work_type, created_at, job_category_id)
VALUES
    ((SELECT id FROM titles WHERE name = 'Financial Analyst'),
     'Jako Financial Analyst będziesz odpowiedzialny za analizę finansową i raportowanie wyników finansowych firmy...',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 56'),
     'REMOTE', '2024-10-01',
     (SELECT id FROM job_categories WHERE name = 'Finanse')),

    ((SELECT id FROM titles WHERE name = 'Software Architect - Cloud Solutions'),
     'W tej roli będziesz odpowiedzialny za projektowanie i wdrażanie architektury chmurowej dla naszych aplikacji...',
     (SELECT id FROM locations WHERE name = 'Wroclaw, PL, 51-502'),
     'HYBRID', '2024-10-02',
     (SELECT id FROM job_categories WHERE name = 'Technologia informacyjna')),

    ((SELECT id FROM titles WHERE name = 'Business Development Manager - International Markets'),
     'Jako Business Development Manager będziesz odpowiedzialny za rozwój biznesu na rynkach międzynarodowych...',
     (SELECT id FROM locations WHERE name = 'Milano, IT, 20151'),
     'STATIONARY', '2024-10-03',
     (SELECT id FROM job_categories WHERE name = 'Sprzedaż i usługi')),

    ((SELECT id FROM titles WHERE name = 'Customer Support Specialist - IT'),
     'W tej roli będziesz odpowiedzialny za wsparcie techniczne klientów oraz rozwiązywanie problemów IT...',
     (SELECT id FROM locations WHERE name = 'Bangalore, IN, 562122'),
     'REMOTE', '2024-10-04',
     (SELECT id FROM job_categories WHERE name = 'Wsparcie techniczne')),

    ((SELECT id FROM titles WHERE name = 'Senior Legal Counsel - Corporate Affairs'),
     'Jako Senior Legal Counsel będziesz odpowiedzialny za doradztwo prawne w zakresie spraw korporacyjnych...',
     (SELECT id FROM locations WHERE name = 'Dublin, VA, US, 24084'),
     'HYBRID', '2024-10-05',
     (SELECT id FROM job_categories WHERE name = 'Prawo i administracja')),

    ((SELECT id FROM titles WHERE name = 'UX/UI Designer - Digital Products'),
     'W tej roli będziesz odpowiedzialny za projektowanie interfejsów użytkownika dla naszych produktów cyfrowych...',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 15'),
     'REMOTE', '2024-10-06',
     (SELECT id FROM job_categories WHERE name = 'Projektowanie produktu')),

    ((SELECT id FROM titles WHERE name = 'Lead Data Scientist - Artificial Intelligence'),
     'Jako Lead Data Scientist będziesz kierować zespołem naukowców zajmujących się sztuczną inteligencją...',
     (SELECT id FROM locations WHERE name = 'Wacol, QLD, AU, 4076'),
     'HYBRID', '2024-10-07',
     (SELECT id FROM job_categories WHERE name = 'Technologia informacyjna')),

    ((SELECT id FROM titles WHERE name = 'Junior Sales Manager - Automotive'),
     'W tej roli będziesz odpowiedzialny za zarządzanie sprzedażą w sektorze motoryzacyjnym...',
     (SELECT id FROM locations WHERE name = 'Eskilstuna, SE, 405 08'),
     'STATIONARY', '2024-10-08',
     (SELECT id FROM job_categories WHERE name = 'Sprzedaż i usługi')),

    ((SELECT id FROM titles WHERE name = 'IT Security Specialist'),
     'Jako IT Security Specialist będziesz odpowiedzialny za zapewnienie bezpieczeństwa informatycznego w firmie...',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 56'),
     'REMOTE', '2024-10-09',
     (SELECT id FROM job_categories WHERE name = 'Technologia informacyjna')),

    ((SELECT id FROM titles WHERE name = 'Production Manager'),
     'Jako Production Manager będziesz zarządzać zespołem produkcyjnym w zakładzie wytwarzającym komponenty dla przemysłu motoryzacyjnego...',
     (SELECT id FROM locations WHERE name = 'Dublin, VA, US, 24084'),
     'HYBRID', '2024-10-10',
     (SELECT id FROM job_categories WHERE name = 'Produkcja'));



INSERT INTO job_posting_recruiters (job_posting_id, recruiter_id) VALUES
                                                                      (1, 1), (1, 2), (1, 5),
                                                                      (2, 2), (2, 4),
                                                                      (3, 1), (3, 4), (3, 5),
                                                                      (4, 2), (4, 3),
                                                                      (5, 1), (5, 5),
                                                                      (6, 3), (6, 4),
                                                                      (7, 2), (7, 5),
                                                                      (8, 1), (8, 3), (8, 4),
                                                                      (9, 2), (9, 3), (9, 5),
                                                                      (10, 1), (10, 4),
                                                                      (11, 2), (11, 5),
                                                                      (12, 3), (12, 4),
                                                                      (13, 1), (13, 2), (13, 5),
                                                                      (14, 3), (14, 4),
                                                                      (15, 1), (15, 3),
                                                                      (16, 2), (16, 5),
                                                                      (17, 1), (17, 4), (17, 5),
                                                                      (18, 2), (18, 3),
                                                                      (19, 1), (19, 2), (19, 4),
                                                                      (20, 3), (20, 5);



INSERT INTO requirements (requirement) VALUES
                                           ('Doświadczenie w zarządzaniu zespołem sprzedażowym.'),
                                           ('Zdolność analitycznego myślenia i planowania.'),
                                           ('Znajomość innowacyjnych narzędzi technologicznych.'),
                                           ('Doświadczenie w planowaniu strategicznym.'),
                                           ('Biegła znajomość języka angielskiego.'),
                                           ('Doświadczenie w pracy z AI i uczeniem maszynowym.'),
                                           ('Biegła znajomość języka angielskiego.'),
                                           ('Znajomość technik negocjacyjnych.'),
                                           ('Kreatywność i zdolność do pracy w zespole.'),
                                           ('Umiejętność pracy w zespole.'),
                                           ('Znajomość zagrożeń bezpieczeństwa IT.'),
                                           ('Zdolności przywódcze i komunikacyjne.');

INSERT INTO job_posting_requirements (job_posting_id, requirement_id) VALUES
                                                                          (1, 1),
                                                                          (1, 2),
                                                                          (1, 3),
                                                                          (2, 4),
                                                                          (2, 5),
                                                                          (2, 6),
                                                                          (3, 7),
                                                                          (3, 8),
                                                                          (3, 9),
                                                                          (4, 10),
                                                                          (4, 11),
                                                                          (4, 12),
                                                                          (5, 1),
                                                                          (5, 2),
                                                                          (5, 3),
                                                                          (6, 4),
                                                                          (6, 5),
                                                                          (7, 6),
                                                                          (7, 7),
                                                                          (8, 8),
                                                                          (8, 9),
                                                                          (9, 10),
                                                                          (9, 11),
                                                                          (10, 12),
                                                                          (10, 1);



-- Dodaj przykładowe adresy do tabeli address
INSERT INTO address (city, "post code", street, "street number", "flat number")
VALUES
    ('Warszawa', '00-001', 'Marszałkowska', 10, 5),
    ('Kraków', '30-002', 'Grochowa', 20, NULL),
    ('Poznań', '60-003', 'Dworcowa', 15, 3),
    ('Wrocław', '50-004', 'Świdnicka', 8, 2),
    ('Gdańsk', '80-005', 'Długa', 12, NULL);

-- Dodaj kandydatów do tabeli candidates, przypisując im adresy
INSERT INTO candidates (first_name, last_name, email, phone, address_id)
VALUES
    ('Jan', 'Kowalski', 'jan.kowalski@example.com', '123-456-789', (SELECT id FROM address WHERE city = 'Warszawa')),
    ('Radzi', 'Nowak', 'radzi2002@wp.pl', '987-654-321', (SELECT id FROM address WHERE city = 'Kraków')),
    ('Radziu', 'Zielinski', 'radziu2402@gmail.com', '555-444-333', (SELECT id FROM address WHERE city = 'Poznań')),
    ('Anna', 'Nowicka', 'anna.nowicka@gmail.com', '111-222-333', (SELECT id FROM address WHERE city = 'Wrocław')),
    ('Piotr', 'Sikorski', 'piotr.sikorski@gmail.com', '444-555-666', (SELECT id FROM address WHERE city = 'Gdańsk'));

-- Dodaj aplikacje do tabeli applications dla istniejącej oferty pracy
INSERT INTO applications (candidate_id, job_posting_id, applied_at, status, rating)
VALUES
    ((SELECT id FROM candidates WHERE email = 'jan.kowalski@example.com'),
     (SELECT id FROM job_postings WHERE offer_code = (SELECT offer_code FROM job_postings WHERE title_id = (SELECT id FROM titles WHERE name = 'Vice President - Remarketing'))),
     '2024-10-12', 'NEW', 0),

    ((SELECT id FROM candidates WHERE email = 'radzi2002@wp.pl'),
     (SELECT id FROM job_postings WHERE offer_code = (SELECT offer_code FROM job_postings WHERE title_id = (SELECT id FROM titles WHERE name = 'Vice President - Remarketing'))),
     '2024-10-12', 'CV_REVIEW', 4),

    ((SELECT id FROM candidates WHERE email = 'radziu2402@gmail.com'),
     (SELECT id FROM job_postings WHERE offer_code = (SELECT offer_code FROM job_postings WHERE title_id = (SELECT id FROM titles WHERE name = 'Vice President - Remarketing'))),
     '2024-10-12', 'CV_REJECTED', 1),

    ((SELECT id FROM candidates WHERE email = 'anna.nowicka@gmail.com'),
     (SELECT id FROM job_postings WHERE offer_code = (SELECT offer_code FROM job_postings WHERE title_id = (SELECT id FROM titles WHERE name = 'Vice President - Remarketing'))),
     '2024-10-12', 'OFFER_MADE', 5),

    ((SELECT id FROM candidates WHERE email = 'piotr.sikorski@gmail.com'),
     (SELECT id FROM job_postings WHERE offer_code = (SELECT offer_code FROM job_postings WHERE title_id = (SELECT id FROM titles WHERE name = 'Vice President - Remarketing'))),
     '2024-10-12', 'APPLICATION_WITHDRAWN', 0);

-- Dodaj dokumenty (CV) do tabeli documents dla każdej aplikacji
INSERT INTO documents (application_id, candidate_id, file_name, file_type, uploaded_at)
VALUES
    ((SELECT id FROM applications WHERE candidate_id = (SELECT id FROM candidates WHERE email = 'jan.kowalski@example.com')),
     (SELECT id FROM candidates WHERE email = 'jan.kowalski@example.com'),
     'jan.kowalski@example.com_CV.pdf', 'PDF', '2024-10-12 19:56:05'),

    ((SELECT id FROM applications WHERE candidate_id = (SELECT id FROM candidates WHERE email = 'radzi2002@wp.pl')),
     (SELECT id FROM candidates WHERE email = 'radzi2002@wp.pl'),
     'radzi2002@wp.pl_CV.pdf', 'PDF', '2024-10-17 19:29:23'),

    ((SELECT id FROM applications WHERE candidate_id = (SELECT id FROM candidates WHERE email = 'radziu2402@gmail.com')),
     (SELECT id FROM candidates WHERE email = 'radziu2402@gmail.com'),
     'radziu2402@gmail.com_CV.pdf', 'PDF', '2024-10-17 19:29:23');


SELECT id FROM applications
WHERE candidate_id = (SELECT id FROM candidates WHERE email = 'jan.kowalski@example.com');


-- Dodanie notatek dla aplikacji o ID 1 (Jan Kowalski)
INSERT INTO notes (application_id, content, created_at)
VALUES
    (1, 'Kandydat ma duże doświadczenie w zarządzaniu projektami.', '2024-10-13'),
    (1, 'Wysoka motywacja do pracy i pozytywne rekomendacje.', '2024-10-14'),
    (1, 'Dobrze oceniają współpracę zespołową.', '2024-10-15');
