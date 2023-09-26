CREATE TABLE `bookmark` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `created_at` datetime(6) DEFAULT NULL,
                            `deleted_at` datetime(6) DEFAULT NULL,
                            `modified_at` datetime(6) DEFAULT NULL,
                            `reservation_date` varchar(255) NOT NULL,
                            `alarm` varchar(255) DEFAULT NULL,
                            `content` varchar(255) DEFAULT NULL,
                            `mt20id` varchar(255) NOT NULL,
                            `user_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_mt20id` (`mt20id`),
                            KEY `FKo4vbqvq5trl11d85bqu5kl870` (`user_id`),
                            CONSTRAINT `FKo4vbqvq5trl11d85bqu5kl870` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci