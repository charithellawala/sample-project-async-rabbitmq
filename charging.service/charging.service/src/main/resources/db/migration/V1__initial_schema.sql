CREATE TABLE if not exists charging_requests (
                                   id UUID PRIMARY KEY,
                                   station_id UUID NOT NULL,
                                   driver_token VARCHAR(255) NOT NULL,
                                   callback_url VARCHAR(500) NOT NULL,
                                   status VARCHAR(50) NOT NULL,
                                   created_at TIMESTAMP NOT NULL,
                                   updated_at TIMESTAMP,
                                   raw_response TEXT
);
