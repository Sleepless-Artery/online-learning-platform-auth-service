CREATE TABLE roles (
  id BIGSERIAL PRIMARY KEY,
  role_name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO roles (role_name) VALUES ('USER'), ('ADMIN'), ('STUDENT'), ('AUTHOR');

CREATE TABLE credentials(
  id BIGSERIAL PRIMARY KEY,
  email_address VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE credentials_roles(
   id BIGSERIAL PRIMARY KEY,
   credential_id BIGINT NOT NULL,
   role_id BIGINT NOT NULL,
   FOREIGN KEY (credential_id) REFERENCES credentials(id),
   FOREIGN KEY (role_id) REFERENCES roles(id)
);