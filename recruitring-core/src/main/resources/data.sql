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
     'Jako Vice President ds. Remarketingu, będziesz odpowiedzialny za rozwijanie i realizację globalnych strategii remarketingowych w ramach naszej organizacji...',
     (SELECT id FROM locations WHERE name = 'Wacol, QLD, AU, 4076'),
     'STATIONARY', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Sprzedaż i usługi')),

    ((SELECT id FROM titles WHERE name = 'Senior Innovation Manager'),
     'Jako Senior Innovation Manager będziesz kluczową osobą odpowiedzialną za identyfikację i wprowadzanie innowacyjnych rozwiązań w naszych produktach i usługach...',
     (SELECT id FROM locations WHERE name = 'Eskilstuna, SE, 405 08'),
     'REMOTE', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Planowanie produktów i technologii')),

    ((SELECT id FROM titles WHERE name = 'Senior Software Node Owner, Electromobility'),
     'W tej roli jako Senior Software Node Owner będziesz odpowiedzialny za pełen cykl życia oprogramowania, z naciskiem na aplikacje w sektorze elektromobilności...',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 15'),
     'HYBRID', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Inżynieria technologii')),

    ((SELECT id FROM titles WHERE name = 'Java Software Engineer (Senior)'),
     'Jako Senior Java Software Engineer będziesz odpowiedzialny za projektowanie i rozwój systemów rozproszonych oraz aplikacji back-endowych w Java...',
     (SELECT id FROM locations WHERE name = 'Wroclaw, PL, 51-502'),
     'REMOTE', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Technologia informacyjna')),

    -- Dalsze wstawianie
    ((SELECT id FROM titles WHERE name = 'Executive Assistant'),
     'Executive Assistant wspiera codzienne operacje zarządu, dbając o koordynację spotkań, zarządzanie kalendarzem oraz organizację podróży służbowych...',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 56'),
     'STATIONARY', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Wsparcie administracyjne i biznesowe')),

    -- Dodaj resztę pozycji analogicznie, używając odpowiednich ID z tytułów, lokalizacji, i kategorii stanowiska
    ((SELECT id FROM titles WHERE name = 'Team Leader - Sales'),
     'Jako Team Leader w dziale sprzedaży, będziesz odpowiedzialny za prowadzenie zespołu sprzedawców oraz monitorowanie i optymalizację wyników sprzedażowych...',
     (SELECT id FROM locations WHERE name = 'Beaufort West, ZA, 6970'),
     'STATIONARY', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Sprzedaż i usługi')),

    ((SELECT id FROM titles WHERE name = 'Production Manager'),
     'Jako Production Manager, będziesz zarządzać zespołem produkcyjnym w zakładzie wytwarzającym komponenty dla przemysłu motoryzacyjnego...',
     (SELECT id FROM locations WHERE name = 'Dublin, VA, US, 24084'),
     'HYBRID', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Produkcja')),

    ((SELECT id FROM titles WHERE name = 'Senior Innovation Engineer'),
     'W roli Senior Innovation Engineer będziesz pracować nad nowatorskimi projektami technologicznymi...',
     (SELECT id FROM locations WHERE name = 'Göteborg, SE, 417 15'),
     'REMOTE', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Inżynieria technologii')),

    ((SELECT id FROM titles WHERE name = 'Data Scientist - Machine Learning'),
     'W tej roli będziesz odpowiedzialny za analizowanie dużych zbiorów danych i rozwijanie algorytmów uczenia maszynowego...',
     (SELECT id FROM locations WHERE name = 'Bangalore, IN, 562122'),
     'HYBRID', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Technologia informacyjna')),

    ((SELECT id FROM titles WHERE name = 'Marketing Director - Europe'),
     'Marketing Director będzie odpowiedzialny za rozwój strategii marketingowej firmy na rynkach europejskich...',
     (SELECT id FROM locations WHERE name = 'Milano, IT, 20151'),
     'REMOTE', '2024-09-23',
     (SELECT id FROM job_categories WHERE name = 'Marketing'));



INSERT INTO job_posting_recruiters (job_posting_id, recruiter_id) VALUES
                                                                      (1, 1), (1, 2),
                                                                      (2, 1), (3, 2),
                                                                      (4, 1), (5, 2),
                                                                      (6, 1), (7, 2),
                                                                      (8, 1), (9, 2),
                                                                      (10, 1);

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
