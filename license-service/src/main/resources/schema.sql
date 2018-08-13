DROP TABLE IF EXISTS `license`;

CREATE TABLE `license` (
  `id` VARCHAR(36) NOT NULL,
  `organization_id` VARCHAR(36) NOT NULL,
  `license_type` VARCHAR(20) NOT NULL,
  `product_name` VARCHAR(50) NOT NULL,
  `license_max` INT(11) NOT NULL,
  `license_allocated` INT(11) NOT NULL,
  `comment` VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `license` (`id`,  `organization_id`, `license_type`, `product_name`, `license_max`, `license_allocated`)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a', 'e254f8c-c442-4ebe-a82a-e2fc1d1ff78a', 'user', 'CustomerPro', 100, 5);
INSERT INTO `license` (`id`,  `organization_id`, `license_type`, `product_name`, `license_max`, `license_allocated`)
VALUES ('t9876f8c-c338-4abc-zf6a-ttt1', 'e254f8c-c442-4ebe-a82a-e2fc1d1ff78a', 'user', 'suitability-plus', 200, 189);
INSERT INTO `license` (`id`,  `organization_id`, `license_type`, `product_name`, `license_max`, `license_allocated`)
VALUES ('38777179-7094-4200-9d61-edb101c6ea84', '442adb6e-fa58-47f3-9ca2-ed1fecdfe86c', 'user', 'HR-PowerSuite', 100, 4);
INSERT INTO `license` (`id`,  `organization_id`, `license_type`, `product_name`, `license_max`, `license_allocated`)
VALUES ('08dbe05-606e-4dad-9d33-90ef10e334f9', '442adb6e-fa58-47f3-9ca2-ed1fecdfe86c', 'core-prod', 'WildCat Application Gateway', 16, 16);
