CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `age` varchar(255) DEFAULT NULL,
                         `gender` varchar(255) DEFAULT NULL,
                         `kakao_email` varchar(255) DEFAULT NULL,
                         `kakao_id` bigint DEFAULT NULL,
                         `nickname` varchar(255) DEFAULT NULL,
                         `role` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_kj2o4jd6ksgt1ebh599lgq2m1` (`kakao_email`),
                         UNIQUE KEY `UK_k4ycaj27putgcujmehwbsrmmc` (`kakao_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
