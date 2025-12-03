-- 1. RESET DỮ LIỆU (Sạch sẽ từ đầu)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE match_detail;
TRUNCATE TABLE matches;
TRUNCATE TABLE ranking;
TRUNCATE TABLE transfer;
TRUNCATE TABLE contract;
TRUNCATE TABLE users;
TRUNCATE TABLE players;
TRUNCATE TABLE teams;
TRUNCATE TABLE tournaments;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. TẠO 8 ĐỘI BÓNG (Đủ cho Cup Tứ kết)
INSERT INTO teams (id, name_team) VALUES 
(1, 'Manchester City'),
(2, 'Arsenal'),
(3, 'Liverpool'),
(4, 'Chelsea'),
(5, 'Man Utd'),
(6, 'Tottenham'),
(7, 'Newcastle'),
(8, 'Aston Villa');

-- 3. TẠO GIẢI ĐẤU (Để trống, bạn sẽ tự tạo Cup trên App)
-- (Hoặc insert sẵn 1 giải Round Robin để test song song)
INSERT INTO tournaments (id, name_tour, formula, start_date, end_date) VALUES 
(1, 'Premier League 24/25', 'round', '2024-08-16', '2025-05-25');

-- 4. TẠO CẦU THỦ (Mỗi đội 2-3 người nổi bật)
INSERT INTO players (id, name_player, birthday, shirt_num, pos) VALUES 
-- Man City (ID 1)
(1, 'Erling Haaland', '2000-07-21', 9, 'ST'),
(2, 'Kevin De Bruyne', '1991-06-28', 17, 'CM'),
-- Arsenal (ID 2)
(3, 'Bukayo Saka', '2001-09-05', 7, 'RW'),
(4, 'Martin Odegaard', '1998-12-17', 8, 'AM'),
-- Liverpool (ID 3)
(5, 'Mohamed Salah', '1992-06-15', 11, 'RW'),
(6, 'Virgil van Dijk', '1991-07-08', 4, 'CB'),
-- Chelsea (ID 4)
(7, 'Cole Palmer', '2002-05-06', 20, 'AM'),
(8, 'Enzo Fernandez', '2001-01-17', 8, 'CM'),
-- Man Utd (ID 5)
(9, 'Bruno Fernandes', '1994-09-08', 8, 'AM'),
(10, 'Marcus Rashford', '1997-10-31', 10, 'LW'),
-- Tottenham (ID 6)
(11, 'Son Heung-min', '1992-07-08', 7, 'LW'),
(12, 'James Maddison', '1996-11-23', 10, 'AM'),
-- Newcastle (ID 7)
(13, 'Alexander Isak', '1999-09-21', 14, 'ST'),
(14, 'Bruno Guimaraes', '1997-11-16', 39, 'CM'),
-- Aston Villa (ID 8)
(15, 'Ollie Watkins', '1995-12-30', 11, 'ST'),
(16, 'Emi Martinez', '1992-09-02', 1, 'GK');

-- 5. TẠO HỢP ĐỒNG (CONTRACT) - Gắn cầu thủ vào đội
INSERT INTO contract (player_id, team_id, start_date, end_date, salary, state) VALUES 
-- Man City
(1, 1, '2022-07-01', '2027-06-30', 375000, 'active'),
(2, 1, '2015-08-30', '2025-06-30', 400000, 'active'),
-- Arsenal
(3, 2, '2019-07-01', '2027-06-30', 195000, 'active'),
(4, 2, '2021-08-20', '2028-06-30', 240000, 'active'),
-- Liverpool
(5, 3, '2017-06-22', '2025-06-30', 350000, 'active'),
(6, 3, '2018-01-01', '2025-06-30', 220000, 'active'),
-- Chelsea
(7, 4, '2023-09-01', '2030-06-30', 130000, 'active'), -- Palmer
(8, 4, '2023-01-31', '2031-06-30', 180000, 'active'),
-- Man Utd
(9, 5, '2020-01-29', '2026-06-30', 240000, 'active'),
(10, 5, '2016-01-01', '2028-06-30', 300000, 'active'),
-- Tottenham
(11, 6, '2015-08-28', '2025-06-30', 190000, 'active'),
(12, 6, '2023-06-28', '2028-06-30', 170000, 'active'),
-- Newcastle
(13, 7, '2022-08-26', '2028-06-30', 120000, 'active'),
(14, 7, '2022-01-30', '2028-06-30', 160000, 'active'),
-- Aston Villa
(15, 8, '2020-09-09', '2028-06-30', 130000, 'active'),
(16, 8, '2020-09-16', '2027-06-30', 120000, 'active');

-- 5.1. Lịch sử hợp đồng cũ (Để demo Palmer từng ở MC)
INSERT INTO contract (player_id, team_id, start_date, end_date, salary, state) VALUES
(7, 1, '2020-07-01', '2023-08-31', 20000, 'expired'); -- Palmer cũ ở MC

-- 6. TẠO USER (USERS)
-- Mỗi đội 1 Manager để bạn test login
INSERT INTO users (id, name_user, hashedpwd, role_user, team_id) VALUES 
(1, 'admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'admin', NULL),
(2, 'mc', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user', 1), -- Man City
(3, 'ars', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user', 2), -- Arsenal
(4, 'liv', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user', 3), -- Liverpool
(5, 'che', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'user', 4); -- Chelsea

-- 7. TẠO DỮ LIỆU CHUYỂN NHƯỢNG (TRANSFER) - Logic Mới (From Seller -> To Buyer)
INSERT INTO transfer (player_id, from_team, to_team, dt, fee, salary_offer, state) VALUES 
-- 7.1. Lịch sử (Accepted): Cole Palmer
-- Player: Palmer (7). From: Man City (1). To: Chelsea (4).
(7, 1, 4, '2023-09-01', 45000000, 130000, 'accepted'),

-- 7.2. Pending (Offer Đang Chờ): Man City hỏi mua Saka của Arsenal
-- Player: Saka (3). 
-- From (Seller - Đội hiện tại): Arsenal (2). 
-- To (Buyer - Đội gửi offer): Man City (1).
(3, 2, 1, CURDATE(), 120000000, 450000, 'pending'),

-- 7.3. Rejected (Bị từ chối): Chelsea hỏi mua Haaland
-- Player: Haaland (1).
-- From (Seller): Man City (1).
-- To (Buyer): Chelsea (4).
(1, 1, 4, '2024-01-01', 150000000, 500000, 'rejected');

-- 8. TẠO TRẬN ĐẤU & RANKING (Cho giải Round Robin ID=1)
-- Trận 1: Man City (1) vs Arsenal (2) -> Hòa 2-2
INSERT INTO matches (id, tour_id, rounds, home_id, away_id, home_result, away_result, dt, isDone) VALUES 
(1, 1, 1, 1, 2, 2, 2, '2024-08-17', true);

INSERT INTO match_detail (match_id, player_id, moment, minutes) VALUES 
(1, 1, 'goal', 10), (1, 2, 'goal', 50), -- MC goal
(1, 3, 'goal', 30), (1, 4, 'goal', 85); -- Ars goal

-- Init Ranking cho giải này
INSERT INTO ranking (tour_id, team_id, points, played, wins, draws, losses, goals, against_goal, diff) VALUES 
(1, 1, 1, 1, 0, 1, 0, 2, 2, 0), -- MC
(1, 2, 1, 1, 0, 1, 0, 2, 2, 0), -- ARS
(1, 3, 0, 0, 0, 0, 0, 0, 0, 0), -- Các đội khác chưa đá
(1, 4, 0, 0, 0, 0, 0, 0, 0, 0),
(1, 5, 0, 0, 0, 0, 0, 0, 0, 0),
(1, 6, 0, 0, 0, 0, 0, 0, 0, 0),
(1, 7, 0, 0, 0, 0, 0, 0, 0, 0),
(1, 8, 0, 0, 0, 0, 0, 0, 0, 0);