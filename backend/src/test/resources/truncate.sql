SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE place_statistics;
TRUNCATE TABLE place_like;
TRUNCATE TABLE place;
TRUNCATE TABLE member;
TRUNCATE TABLE player;
TRUNCATE TABLE game_result;
TRUNCATE TABLE game;
TRUNCATE TABLE hint;
TRUNCATE TABLE letter;
TRUNCATE TABLE read_letter_log;
TRUNCATE TABLE write_letter_log;

SET FOREIGN_KEY_CHECKS = 1;
