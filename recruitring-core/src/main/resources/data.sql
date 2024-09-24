INSERT INTO users (login, email, password, role)
VALUES ('admin', 'admin@example.com', '$2a$10$paWcoAGkFwu.rAeNNcfPv.FfRelOjwSuYw/iacp3HCbLpkuJN86iO', 'ADMINISTRATOR');

INSERT INTO users (login, email, password, role)
VALUES ('user', 'user@example.com', '$2a$10$paWcoAGkFwu.rAeNNcfPv.FfRelOjwSuYw/iacp3HCbLpkuJN86iO', 'RECRUITER');

INSERT INTO recruiters (first_name, last_name, position, user_id)
VALUES ('Admin', 'Example', 'HR Manager',
        (SELECT id FROM users WHERE login = 'admin'));

INSERT INTO recruiters (first_name, last_name, position, user_id)
VALUES ('User', 'Example', 'Recruiter',
        (SELECT id FROM users WHERE login = 'user'));

INSERT INTO job_postings (title, description, location, work_type, created_at, job_category)
VALUES
    ('Vice President - Remarketing',
     'Jako Vice President ds. Remarketingu, będziesz odpowiedzialny za rozwijanie i realizację globalnych strategii remarketingowych w ramach naszej organizacji...',
     'Wacol, QLD, AU, 4076', 'STATIONARY', '2024-09-23', 'Sprzedaż i usługi'),

    ('Senior Innovation Manager',
     'Jako Senior Innovation Manager będziesz kluczową osobą odpowiedzialną za identyfikację i wprowadzanie innowacyjnych rozwiązań w naszych produktach i usługach...',
     'Eskilstuna, SE, 405 08', 'REMOTE', '2024-09-23', 'Planowanie produktów i technologii'),

    ('Senior Software Node Owner, Electromobility',
     'W tej roli jako Senior Software Node Owner będziesz odpowiedzialny za pełen cykl życia oprogramowania, z naciskiem na aplikacje w sektorze elektromobilności...',
     'Göteborg, SE, 417 15', 'HYBRID', '2024-09-23', 'Inżynieria technologii'),

    ('Java Software Engineer (Senior)',
     'Jako Senior Java Software Engineer będziesz odpowiedzialny za projektowanie i rozwój systemów rozproszonych oraz aplikacji back-endowych w Java...',
     'Wroclaw, PL, 51-502', 'REMOTE', '2024-09-23', 'Technologia informacyjna'),

    ('Executive Assistant',
     'Executive Assistant wspiera codzienne operacje zarządu, dbając o koordynację spotkań, zarządzanie kalendarzem oraz organizację podróży służbowych...',
     'Göteborg, SE, 417 56', 'STATIONARY', '2024-09-23', 'Wsparcie administracyjne i biznesowe'),

    ('Team Leader - Sales',
     'Jako Team Leader w dziale sprzedaży, będziesz odpowiedzialny za prowadzenie zespołu sprzedawców oraz monitorowanie i optymalizację wyników sprzedażowych...',
     'Beaufort West, ZA, 6970', 'STATIONARY', '2024-09-23', 'Sprzedaż i usługi'),

    ('Production Manager',
     'Jako Production Manager, będziesz zarządzać zespołem produkcyjnym w zakładzie wytwarzającym komponenty dla przemysłu motoryzacyjnego...',
     'Dublin, VA, US, 24084', 'HYBRID', '2024-09-23', 'Produkcja'),

    ('Senior Innovation Engineer',
     'W roli Senior Innovation Engineer będziesz pracować nad nowatorskimi projektami technologicznymi...',
     'Göteborg, SE, 417 15', 'REMOTE', '2024-09-23', 'Inżynieria technologii'),

    ('Data Scientist - Machine Learning',
     'W tej roli będziesz odpowiedzialny za analizowanie dużych zbiorów danych i rozwijanie algorytmów uczenia maszynowego...',
     'Bangalore, IN, 562122', 'HYBRID', '2024-09-23', 'Technologia informacyjna'),

    ('Marketing Director - Europe',
     'Marketing Director będzie odpowiedzialny za rozwój strategii marketingowej firmy na rynkach europejskich...',
     'Milano, IT, 20151', 'REMOTE', '2024-09-23', 'Marketing');


INSERT INTO job_posting_recruiters (job_posting_id, recruiter_id) VALUES
                                                                 (1, 1), (1, 2),
                                                                 (2, 1), (3, 2),
                                                                 (4, 1), (5, 2),
                                                                 (6, 1), (7, 2),
                                                                 (8, 1), (9, 2),
                                                                 (10, 1);


INSERT INTO job_postings (title, description, location, work_type, created_at, job_category)
VALUES
    ('Product Manager - Industrial Products',
     'Jako Product Manager będziesz odpowiedzialny za rozwój produktów przemysłowych, współpracując z zespołami inżynierów i sprzedawców.',
     'Göteborg, SE, 417 15', 'HYBRID', '2024-09-24', 'Zarządzanie produktem'),

    ('Lead Data Scientist - Artificial Intelligence',
     'Będziesz prowadził zespół naukowców zajmujących się sztuczną inteligencją, dostarczając innowacyjne rozwiązania w zakresie uczenia maszynowego.',
     'Bangalore, IN, 562122', 'REMOTE', '2024-09-24', 'Zaawansowana analityka danych'),

    ('Junior Sales Manager - Automotive',
     'Odpowiedzialność za sprzedaż i współpracę z kluczowymi klientami w branży motoryzacyjnej, dbanie o rozwój relacji biznesowych.',
     'Wroclaw, PL, 51-502', 'STATIONARY', '2024-09-24', 'Sprzedaż i usługi'),

    ('IT Security Specialist',
     'Odpowiedzialny za zabezpieczanie systemów informatycznych, analizę zagrożeń i wdrażanie środków ochrony.',
     'Göteborg, SE, 417 15', 'REMOTE', '2024-09-24', 'Technologia informacyjna'),

    ('UX/UI Designer - Digital Products',
     'Projektowanie interfejsów użytkownika i doświadczeń użytkownika dla produktów cyfrowych, praca nad innowacyjnymi rozwiązaniami.',
     'Milano, IT, 20151', 'HYBRID', '2024-09-24', 'Projektowanie produktu'),

    ('Financial Analyst',
     'Analiza finansowa dla projektów strategicznych, raportowanie wyników i współpraca z zespołami finansowymi i operacyjnymi.',
     'Göteborg, SE, 417 56', 'STATIONARY', '2024-09-24', 'Finanse'),

    ('Software Architect - Cloud Solutions',
     'Odpowiedzialność za projektowanie i wdrażanie architektur opartych na chmurze, współpraca z zespołami developerskimi.',
     'Bangalore, IN, 562122', 'REMOTE', '2024-09-24', 'Inżynieria technologii'),

    ('Business Development Manager - International Markets',
     'Zarządzanie rozwojem biznesu na rynkach międzynarodowych, współpraca z globalnymi partnerami i klientami.',
     'Wroclaw, PL, 51-502', 'HYBRID', '2024-09-24', 'Sprzedaż i usługi'),

    ('Customer Support Specialist - IT',
     'Wsparcie techniczne dla użytkowników systemów informatycznych, rozwiązywanie problemów technicznych i udzielanie porad.',
     'Göteborg, SE, 417 15', 'STATIONARY', '2024-09-24', 'Wsparcie techniczne'),

    ('Senior Legal Counsel - Corporate Affairs',
     'Zapewnienie wsparcia prawnego dla zarządu i kluczowych decyzji biznesowych, doradztwo w zakresie prawa korporacyjnego.',
     'Milano, IT, 20151', 'REMOTE', '2024-09-24', 'Prawo i administracja');

INSERT INTO job_posting_recruiters (job_posting_id, recruiter_id) VALUES
                                                                 (11, 1), (11, 2),
                                                                 (12, 1), (13, 2),
                                                                 (14, 1), (15, 2),
                                                                 (16, 1), (17, 2),
                                                                 (18, 1), (19, 2),
                                                                 (20, 1);

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
