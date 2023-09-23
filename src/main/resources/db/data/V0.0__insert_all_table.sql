INSERT INTO `users` (`age`, `gender`, `kakao_email`, `kakao_id`, `nickname`, `role`)
VALUES
    ('25', 'Male', 'johnd@kakao.com', 0, 'JohnD', 'User'),
    ('30', 'Female', 'alices@kakao.com', 1, 'AliceS', 'User'),
    ('28', 'Male', 'bobj@kakao.com', 2, 'BobJ', 'User'),
    ('32', 'Female', 'charlieb@kakao.com', 3, 'CharlieB', 'User'),
    ('27', 'Male', 'evew@kakao.com', 4, 'EveW', 'User'),
    ('29', 'Female', 'frankb@kakao.com', 5, 'FrankB', 'User');
--

INSERT INTO `name_with_mt20id` (`mt20id`, `name`) VALUES
                                                      ('PF217338', 'Name1'),
                                                      ('PF217311', 'Name2'),
                                                      ('PF217334', 'Name3'),
                                                      ('PF217111', 'Name4'),
                                                      ('PF219261', 'Name5'),
                                                      ('PF217047', 'Name6');

--

INSERT INTO `detail_info`
(mt20id, entrpsnm, fcltynm, genrenm, mt10id, pcseguidance, prfage, prfcast, prfcrew, prfnm, prfruntime)
VALUES
    ('PF217338', 'Name1', 'FacilityA', 'GenreA', 'MT10A', 'GuidanceA', '20+', 'CastA', 'CrewA', 'PerformanceA', '120min'),
    ('PF217311', 'Name2', 'FacilityB', 'GenreB', 'MT10B', 'GuidanceB', '25+', 'CastB', 'CrewB', 'PerformanceB', '110min'),
    ('PF217334', 'Name3', 'FacilityC', 'GenreC', 'MT10C', 'GuidanceC', '18+', 'CastC', 'CrewC', 'PerformanceC', '105min'),
    ('PF217111', 'Name4', 'FacilityD', 'GenreD', 'MT10D', 'GuidanceD', '22+', 'CastD', 'CrewD', 'PerformanceD', '115min'),
    ('PF219261', 'Name5', 'FacilityE', 'GenreE', 'MT10E', 'GuidanceE', '24+', 'CastE', 'CrewE', 'PerformanceE', '125min'),
    ('PF217047', 'Name6', 'FacilityF', 'GenreF', 'MT10F', 'GuidanceF', '23+', 'CastF', 'CrewF', 'PerformanceF', '130min');


--

INSERT INTO `bookmark`
(created_at, reservation_date, mt20id, user_id, alarm, content)
VALUES
    (NOW(), '2023-09-25', 'PF217338', 22, 'AlarmA', 'ContentA'),
    (NOW(), '2023-09-26', 'PF217311', 23, 'AlarmB', 'ContentB'),
    (NOW(), '2023-09-27', 'PF217334', 24, 'AlarmC', 'ContentC'),
    (NOW(), '2023-09-28', 'PF217111', 25, 'AlarmD', 'ContentD'),
    (NOW(), '2023-09-29', 'PF219261', 26, 'AlarmE', 'ContentE'),
    (NOW(), '2023-09-30', 'PF217047', 27, 'AlarmF', 'ContentF');