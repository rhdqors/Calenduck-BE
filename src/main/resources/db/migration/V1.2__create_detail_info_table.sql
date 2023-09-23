CREATE TABLE `detail_info` (
                               `mt20id` varchar(255) NOT NULL,
                               `entrpsnm` varchar(255) DEFAULT NULL,
                               `fcltynm` varchar(255) DEFAULT NULL,
                               `genrenm` varchar(255) DEFAULT NULL,
                               `mt10id` varchar(255) DEFAULT NULL,
                               `pcseguidance` varchar(255) DEFAULT NULL,
                               `prfage` varchar(255) DEFAULT NULL,
                               `prfcast` varchar(255) DEFAULT NULL,
                               `prfcrew` varchar(255) DEFAULT NULL,
                               `prfnm` varchar(255) DEFAULT NULL,
                               `prfruntime` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`mt20id`),
                               KEY `idx_mt20id` (`mt20id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci