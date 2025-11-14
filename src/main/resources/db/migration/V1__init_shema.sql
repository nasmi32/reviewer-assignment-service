CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(100) NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE teams(
    team_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    users
);