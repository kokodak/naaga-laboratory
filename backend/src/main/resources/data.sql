-- member 테이블에 데이터 삽입
INSERT INTO member (deleted, created_at, updated_at, email)
VALUES (0, '2023-01-01 12:00:00.000000', NULL, 'member1@example.com'),
       (0, '2023-01-02 14:30:00.000000', NULL, 'member2@example.com'),
       (0, '2023-01-03 18:45:00.000000', NULL, 'member3@example.com');

-- player 테이블에 데이터 삽입
INSERT INTO player (deleted, total_score, created_at, member_id, updated_at, nickname)
VALUES (0, 100, '2023-01-01 12:30:00.000000', 1, NULL, 'player1'),
       (0, 150, '2023-01-02 15:00:00.000000', 2, NULL, 'player2'),
       (0, 120, '2023-01-03 19:00:00.000000', 3, NULL, 'player3');

-- place 테이블에 데이터 삽입
INSERT INTO place (latitude, longitude, created_at, player_id, updated_at, description, image_url, name)
VALUES (37.123456, -122.987654, '2023-01-01 13:00:00.000000', 1, NULL, 'A beautiful place',
        'https://example.com/image1.jpg', 'Place1'),
       (38.654321, -121.876543, '2023-01-02 16:00:00.000000', 2, NULL, 'Scenic spot with a view',
        'https://example.com/image2.jpg', 'Place2'),
       (39.987654, -120.765432, '2023-01-03 20:00:00.000000', 3, NULL, 'Historical landmark',
        'https://example.com/image3.jpg', 'Place3');
