DROP TABLE IF EXISTS `organization`;

CREATE TABLE `organization` (
  `id` VARCHAR(36) NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `contact_name` VARCHAR(32) NOT NULL,
  `contact_email` VARCHAR(50) NOT NULL,
  `contact_phone` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `organization` (`id`, `name`, `contact_name`, `contact_email`, `contact_phone`)
VALUES ('e254f8c-c442-4ebe-a82a-e2fc1d1ff78a', 'customer-crm-co', 'Mark Balster', 'mark.balster@custcrmco.com', '823-555-1212');

INSERT INTO `organization` (`id`, `name`, `contact_name`, `contact_email`, `contact_phone`)
VALUES ('442adb6e-fa58-47f3-9ca2-ed1fecdfe86c', 'HR-PowerSuite', 'Doug Drewry', 'doug.drewry@hr.com', '920-555-1212');
