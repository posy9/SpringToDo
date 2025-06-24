CREATE TABLE task
(
    id          BIGSERIAL PRIMARY KEY,
    title       varchar(100) NOT NULL,
    description TEXT,
    status      VARCHAR(15),
    user_id     BIGINT references user (id)

)
CREATE TABLE user
(
    id       BIGSERIAL PRIMARY KEY,
    username varchar(30) NOT NULL UNIQUE
)
