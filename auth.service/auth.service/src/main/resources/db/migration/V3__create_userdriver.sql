CREATE TABLE if not exists user_driver (
    driver_token VARCHAR(80) PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

