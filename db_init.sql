CREATE TYPE course_level AS ENUM ('A1', 'A2', 'B1', 'B2', 'C1', 'C2');


CREATE TABLE users (
                       username VARCHAR(50) PRIMARY KEY,
                       pwd_hash VARCHAR(100) NOT NULL
);

CREATE TABLE courses (
                         id serial PRIMARY KEY,
                         title VARCHAR(200) NOT NULL,
                         level course_level NOT NULL,
                         description VARCHAR(3000) NOT NULL
);

CREATE TABLE course_modules (
                                id serial PRIMARY KEY,
                                eta int NOT NULL,
                                title VARCHAR(200) NOT NULL,
                                materials TEXT
);