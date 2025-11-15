CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TYPE pr_status AS ENUM ('OPEN', 'MERGED');

CREATE TABLE teams
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username  VARCHAR(100) NOT NULL UNIQUE,
    is_active BOOLEAN      NOT NULL,
    team_id   UUID,
    CONSTRAINT fk_user_team
        FOREIGN KEY (team_id)
            REFERENCES teams (id)
            ON DELETE SET NULL
);

CREATE TABLE pull_request
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title     VARCHAR(255) NOT NULL,
    author_id UUID         NOT NULL,
    status    pr_status    NOT NULL,
    merged_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_pr_author
        FOREIGN KEY (author_id)
            REFERENCES users (id)
);

CREATE TABLE pull_request_review
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pull_request_id UUID NOT NULL,
    reviewer_id     UUID NOT NULL,

    CONSTRAINT fk_pr_review_pr
        FOREIGN KEY (pull_request_id)
            REFERENCES pull_request (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_pr_review_user
        FOREIGN KEY (reviewer_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_pr_reviewer UNIQUE (pull_request_id, reviewer_id)
);