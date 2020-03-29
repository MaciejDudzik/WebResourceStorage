CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE web_resource
(
    id           uuid               DEFAULT uuid_generate_v4() PRIMARY KEY,
    url          VARCHAR   NOT NULL,
    date         timestamp NOT NULL DEFAULT now(),
    content      bytea     NOT NULL,
    content_type varchar
);
