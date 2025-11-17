CREATE DATABASE IF NOT EXISTS `aircraft_maintenance`;

USE `aircraft_maintenance`;

DROP TABLE IF EXISTS `replacement_parts`;
DROP TABLE IF EXISTS `maintenance_periods`;
DROP TABLE IF EXISTS `hangers`;
DROP TABLE IF EXISTS `fleet`;
DROP TABLE IF EXISTS `aircraft_capacities`;

CREATE TABLE `aircraft_capacities` (
    `model` VARCHAR(10) NOT NULL,
    `capacity` INT NOT NULL,
    CONSTRAINT `pk_aircraft_capacity` PRIMARY KEY (`model`)
);

CREATE TABLE `fleet` (
    `registration` VARCHAR(10) NOT NULL,
    `model` VARCHAR(10) NOT NULL,
    `status` ENUM('in service', 'under maintenance') NOT NULL,
    CONSTRAINT `pk_fleet` PRIMARY KEY (`registration`),
    CONSTRAINT `fk_fleet_aircraft_capacity` FOREIGN KEY (`model`)
		REFERENCES `aircraft_capacities`(`model`)
);

CREATE TABLE `hangers` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `location` VARCHAR(255) NOT NULL,
    `status` ENUM('available', 'occupied') NOT NULL,
    CONSTRAINT `pk_hanger` PRIMARY KEY (`id`)
);

CREATE TABLE `maintenance_periods` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `type` VARCHAR(25) NOT NULL,
    `aircraft_registration` VARCHAR(10) NOT NULL,
    `hanger_id` INT NOT NULL,
    `status` ENUM('ongoing', 'completed') NOT NULL,
    `started_at` DATE NOT NULL,
    `completed_at` DATE,
    CONSTRAINT `pk_maintenance_period` PRIMARY KEY (`id`),
    CONSTRAINT `fk_maintenance_period_fleet` FOREIGN KEY (`aircraft_registration`)
		REFERENCES `fleet`(`registration`),
    CONSTRAINT `fk_maintenance_period_hanger` FOREIGN KEY (`hanger_id`)
		REFERENCES `hangers`(`id`)
);

CREATE TABLE `replacement_parts` (
    `number` INT NOT NULL,
    `name` VARCHAR(10) NOT NULL,
    `amount` INT NOT NULL,
    `aircraft_registration` VARCHAR(10) NOT NULL,
    `maintenance_id` INT NOT NULL,
    CONSTRAINT `pk_replacement_part` PRIMARY KEY (`number` , `maintenance_id`),
    CONSTRAINT `fk_replacement_part_fleet` FOREIGN KEY (`aircraft_registration`)
        REFERENCES `fleet` (`registration`),
    CONSTRAINT `fk_replacement_part_maintenance_period` FOREIGN KEY (`maintenance_id`)
        REFERENCES `maintenance_periods` (`id`)
);
