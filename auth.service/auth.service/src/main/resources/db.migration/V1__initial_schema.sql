CREATE TABLE if not exists authorization_reports (
                                       id UUID PRIMARY KEY,
                                       station_id UUID NOT NULL,
                                       driver_token VARCHAR(255) NOT NULL,
                                       decision VARCHAR(50) NOT NULL,
                                       created_at TIMESTAMP NOT NULL
);
