CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username varchar(30) NOT NULL UNIQUE
);
CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY,
    title       varchar(100) NOT NULL,
    description TEXT,
    status      VARCHAR(15),
    userId      BIGINT       NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (userId) REFERENCES users (id) ON DELETE CASCADE
)

