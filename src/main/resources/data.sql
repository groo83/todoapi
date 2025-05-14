-- 유저 초기 데이터
INSERT INTO users (id, email, nickname, password, created_at, updated_at)
VALUES
 (1, 'test@init.com', 'testuser', '$2a$10$qemV6prA3XlWhNIT7SzAOeczkUIXZrch3IEtfGOfg54MNUvhPOdnW', datetime('now'), datetime('now')),
 (2, 'test2@init.com', 'testuser2', '$2a$10$yBJiYgmyOsp6VL4HD3G6R.T3ui1vpAPvb7Rybfq7aeHxARMgFjkGK', datetime('now'), datetime('now'));

-- 할일 초기 데이터
INSERT INTO todos (id, title, description, completed, user_id, created_at, updated_at)
VALUES
  (1, '할 일 1', '첫 번째 항목입니다.', 0, 1, datetime('now'), datetime('now')),
  (2, '할 일 2', '두 번째 항목입니다.', 1, 1, datetime('now'), datetime('now')),
  (3, '할 일 3', '세 번째 항목입니다.', 1, 2, datetime('now'), datetime('now'));