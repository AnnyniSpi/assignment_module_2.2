CREATE TABLE writer
(
    id            INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstname     VARCHAR(100),
    lastname      VARCHAR(100),
    status VARCHAR(50)
);

CREATE TABLE post
(
    id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content     TEXT,
    created     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50),
    writer_id   BIGINT REFERENCES writer(id)

);

CREATE TABLE label
(
    id           INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100),
    status VARCHAR(50)
);

CREATE TABLE post_label
(
    post_id  BIGINT REFERENCES module2_2.post(id),
    label_id BIGINT REFERENCES module2_2.label(id),
    PRIMARY KEY (post_id, label_id)
);