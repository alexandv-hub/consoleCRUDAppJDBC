CREATE TYPE status_type AS ENUM ('ACTIVE', 'DELETED');

CREATE TABLE label (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL UNIQUE,
   status status_type NOT NULL DEFAULT 'ACTIVE'
);

CREATE TYPE post_status_type AS ENUM ('ACTIVE', 'UNDER_REVIEW', 'DELETED');

CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP,
    post_status post_status_type NOT NULL DEFAULT 'UNDER_REVIEW',
    status status_type NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE post_label (
    post_id BIGINT NOT NULL,
    label_id BIGINT NOT NULL,
    status status_type NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (label_id) REFERENCES label(id),
    UNIQUE (post_id, label_id)
);

CREATE TABLE writer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    status status_type NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE writer_post (
    writer_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    status status_type NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (writer_id) REFERENCES writer(id),
    FOREIGN KEY (post_id) REFERENCES post(id),
    UNIQUE (writer_id, post_id)
);
