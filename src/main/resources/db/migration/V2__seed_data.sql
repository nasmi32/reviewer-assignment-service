INSERT INTO teams (id, name)
VALUES (gen_random_uuid(), 'Backend Avengers'),
       (gen_random_uuid(), 'Frontend Ninjas'),
       (gen_random_uuid(), 'DevOps Masters'),
       (gen_random_uuid(), 'QA Legends');

WITH t AS (SELECT id, name
           FROM teams)
INSERT
INTO users (id, username, is_active, team_id)
VALUES
    -- Backend Avengers (Team 1)
    (gen_random_uuid(), 'alice', TRUE, (SELECT id FROM teams WHERE name = 'Backend Avengers')),
    (gen_random_uuid(), 'bob', TRUE, (SELECT id FROM teams WHERE name = 'Backend Avengers')),
    (gen_random_uuid(), 'charlie', FALSE, (SELECT id FROM teams WHERE name = 'Backend Avengers')),

    -- Frontend Ninjas (Team 2)
    (gen_random_uuid(), 'diana', TRUE, (SELECT id FROM teams WHERE name = 'Frontend Ninjas')),
    (gen_random_uuid(), 'eva', TRUE, (SELECT id FROM teams WHERE name = 'Frontend Ninjas')),

    -- DevOps Masters (Team 3)
    (gen_random_uuid(), 'frank', TRUE, (SELECT id FROM teams WHERE name = 'DevOps Masters')),
    (gen_random_uuid(), 'george', FALSE, (SELECT id FROM teams WHERE name = 'DevOps Masters')),

    -- QA Legends (Team 4)
    (gen_random_uuid(), 'helen', TRUE, (SELECT id FROM teams WHERE name = 'QA Legends')),
    (gen_random_uuid(), 'ivan', TRUE, (SELECT id FROM teams WHERE name = 'QA Legends')),
    (gen_random_uuid(), 'john', FALSE, (SELECT id FROM teams WHERE name = 'QA Legends'));