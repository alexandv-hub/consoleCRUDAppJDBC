CREATE TABLE label (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    status ENUM('ACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content VARCHAR(255) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP,
    post_status ENUM('ACTIVE','UNDER_REVIEW', 'DELETED') NOT NULL DEFAULT 'UNDER_REVIEW',
    status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE post_label (
    post_id BIGINT NOT NULL,
    label_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (label_id) REFERENCES label(id),
    UNIQUE (post_id, label_id)
);

CREATE TABLE writer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE writer_post (
     writer_id BIGINT NOT NULL,
     post_id BIGINT NOT NULL,
     status ENUM('ACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
     FOREIGN KEY (writer_id) REFERENCES writer(id),
     FOREIGN KEY (post_id) REFERENCES post(id),
     UNIQUE (writer_id, post_id)
);
