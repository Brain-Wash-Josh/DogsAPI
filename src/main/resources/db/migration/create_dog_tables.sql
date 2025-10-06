CREATE TABLE dog_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE leaving_reason (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reason_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE dog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    breed VARCHAR(255) NOT NULL,
    supplier VARCHAR(255) NOT NULL,
    badge_id VARCHAR(100),
    gender VARCHAR(10) NOT NULL,
    birth_date DATE NOT NULL,
    date_acquired DATE NOT NULL,
    status_id BIGINT NOT NULL,
    leaving_date DATE,
    leaving_reason_id BIGINT,
    kennelling_characteristic TEXT,
    deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES dog_status(id),
    FOREIGN KEY (leaving_reason_id) REFERENCES leaving_reason(id)
);

-- Insert default statuses
INSERT INTO dog_status (status_name) VALUES 
    ('In Training'),
    ('In Service'),
    ('Retired'),
    ('Left');

-- Insert default leaving reasons
INSERT INTO leaving_reason (reason_name) VALUES 
    ('Transferred'),
    ('Retired (Put Down)'),
    ('KIA'),
    ('Rejected'),
    ('Retired (Re-housed)'),
    ('Died');