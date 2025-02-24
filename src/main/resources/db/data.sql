-- 100만 명의 회원 생성
INSERT INTO member (email, password, nickname, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT /*+ SET_VAR(cte_max_recursion_depth = 1000000) */
    1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1000000
)
SELECT
    CONCAT('user', LPAD(n, 7, '0'), '@test.com'),
    '$2a$10$iWPQvWHXRaJvPpZn1qzp3.GKBHXa9mFQXj4.Nz.yu.YyXJxvxXnwi',
    CONCAT('User', LPAD(n, 7, '0')),
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY),
    NOW()
FROM numbers;

-- 50만 개 데이터 삽입
INSERT INTO todo (member_id, category, date,created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT /*+ SET_VAR(cte_max_recursion_depth = 500000) */
    1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 500000
)
SELECT
    1 + MOD(n, 1000000), -- member_id (1~100만까지 랜덤 배정)
    CASE MOD(n, 4)
        WHEN 0 THEN 'WORK'
        WHEN 1 THEN 'LIFE'
        WHEN 2 THEN 'HEALTH'
        WHEN 3 THEN 'STUDY'
    END, -- category (EnumType.STRING)
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY) ,-- date (과거 1년 내 랜덤)
    NOW(),
    NOW()
FROM numbers;

-- 150만 개의 TodoTask 데이터 삽입 (각 투두에 평균 3개)
INSERT INTO todo_task (todo_id, task, is_done)
WITH RECURSIVE numbers AS (
    SELECT 1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1500000
)
SELECT
    1 + MOD(n, 500000), -- todo_id (1~50만 중 랜덤 배정)
    CONCAT('Task ', n, ' for Todo ', MOD(n, 500000)), -- task 내용
    FALSE -- isDone (초기값 false)
FROM numbers;


-- 10000개의 태그 생성
INSERT INTO tag (name)
WITH RECURSIVE numbers AS (
    SELECT /*+ SET_VAR(cte_max_recursion_depth = 10000) */
    1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 10000
)
SELECT CONCAT('tag', n)
FROM numbers;

-- 100000개의 투두-태그 연결
INSERT INTO todo_tag (todo_id, tag_id)
WITH RECURSIVE numbers AS (
    SELECT /*+ SET_VAR(cte_max_recursion_depth = 200000) */
    1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 200000
)
SELECT DISTINCT
    1 + MOD(FLOOR(n/4), 500000) as todo_id,  -- 평균적으로 각 투두당 4개의 태그
    1 + MOD(FLOOR(RAND() * 10000), 10000) as tag_id
FROM numbers
GROUP BY todo_id, tag_id;

-- 100000개의 좋아요
--INSERT INTO ch2_board_likes (member_id, board_id)
--WITH RECURSIVE numbers AS (
--    SELECT /*+ SET_VAR(cte_max_recursion_depth = 200000) */
--    1 as n
--    UNION ALL
--    SELECT n + 1 FROM numbers WHERE n < 200000
--)
--SELECT DISTINCT
--    1 + MOD(FLOOR(RAND() * 1000000), 1000000) as member_id,
--    1 + MOD(FLOOR(RAND() * 500000), 500000) as board_id
--FROM numbers
--GROUP BY member_id, board_id
--LIMIT 100000;