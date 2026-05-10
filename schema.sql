-- Fleet Fuel Management System - Database Schema
-- Run this in MySQL to set up the database

CREATE DATABASE IF NOT EXISTS fleet_db;
USE fleet_db;

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_number VARCHAR(20) NOT NULL UNIQUE,
    vehicle_type VARCHAR(20) NOT NULL,         -- TRUCK, VAN, CAR, BIKE
    driver_name VARCHAR(100) NOT NULL,
    tank_capacity_litres DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE, INACTIVE, MAINTENANCE
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    INDEX idx_status (status),
    INDEX idx_vehicle_type (vehicle_type)
);

-- Fuel logs table
CREATE TABLE IF NOT EXISTS fuel_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    fuel_date DATE NOT NULL,
    litres_filled DOUBLE NOT NULL,
    cost_per_litre DOUBLE NOT NULL,
    total_cost DOUBLE,
    odometer_reading DOUBLE NOT NULL,
    distance_since_last_fill DOUBLE,
    fuel_efficiency DOUBLE,           -- km per litre
    fuel_station VARCHAR(100),
    notes TEXT,
    logged_at DATETIME NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
    INDEX idx_vehicle_fuel (vehicle_id),
    INDEX idx_fuel_date (fuel_date)
);

-- Sample Data
INSERT INTO vehicles (registration_number, vehicle_type, driver_name, tank_capacity_litres, status, created_at, updated_at)
VALUES
    ('AP31AB1234', 'TRUCK', 'Ravi Kumar', 200.0, 'ACTIVE', NOW(), NOW()),
    ('AP31CD5678', 'VAN', 'Suresh Babu', 60.0, 'ACTIVE', NOW(), NOW()),
    ('AP31EF9012', 'CAR', 'Priya Sharma', 45.0, 'MAINTENANCE', NOW(), NOW());

INSERT INTO fuel_logs (vehicle_id, fuel_date, litres_filled, cost_per_litre, total_cost, odometer_reading, distance_since_last_fill, fuel_efficiency, fuel_station, logged_at)
VALUES
    (1, '2025-01-10', 150.0, 95.50, 14325.0, 12000.0, NULL, NULL, 'HP Petrol Pump, Vijayawada', NOW()),
    (1, '2025-02-05', 140.0, 96.00, 13440.0, 13850.0, 1850.0, 13.21, 'Indian Oil, Guntur', NOW()),
    (2, '2025-01-15', 50.0, 95.50, 4775.0, 8500.0, NULL, NULL, 'BPCL, Vijayawada', NOW()),
    (2, '2025-02-10', 48.0, 96.00, 4608.0, 9200.0, 700.0, 14.58, 'HP Petrol Pump, Vijayawada', NOW());

-- Useful Queries
-- 1. Total fuel cost per vehicle
SELECT v.registration_number, v.driver_name, SUM(f.total_cost) AS total_cost
FROM fuel_logs f JOIN vehicles v ON f.vehicle_id = v.id
GROUP BY v.id, v.registration_number, v.driver_name ORDER BY total_cost DESC;

-- 2. Average fuel efficiency per vehicle
SELECT v.registration_number, AVG(f.fuel_efficiency) AS avg_km_per_litre
FROM fuel_logs f JOIN vehicles v ON f.vehicle_id = v.id
WHERE f.fuel_efficiency IS NOT NULL
GROUP BY v.id, v.registration_number;

-- 3. Monthly fleet cost report
SELECT DATE_FORMAT(fuel_date, '%Y-%m') AS month, SUM(total_cost) AS monthly_cost
FROM fuel_logs GROUP BY month ORDER BY month DESC;
