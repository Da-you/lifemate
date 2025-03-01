-- cte 문제 발생시
-- SHOW VARIABLES LIKE 'cte_max_recursion_depth'; cte_max 확인
-- SET SESSION cte_max_recursion_depth = 1000000; cte_max 조정

-- 150만 명의 회원(멘토 100만, 일반 50만) 생성--
INSERT INTO member (email, password, nickname, role, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT /*+ SET_VAR(cte_max_recursion_depth = 1500000) */
    1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1500000
)
SELECT
    CONCAT('user', LPAD(n, 7, '0'), '@test.com') AS email , -- email
    '$2a$10$iWPQvWHXRaJvPpZn1qzp3.GKBHXa9mFQXj4.Nz.yu.YyXJxvxXnwi' AS password, -- 비번
    CONCAT('User', LPAD(n, 7, '0')) AS nickname, -- 닉네임
    CASE
        WHEN n <= 1000000 THEN 'ROLE_MENTOR'  -- 100만 명까지는 멘토 유저
        ELSE 'ROLE_USER'  -- 이후 50만 명은 일반 유저
    END AS role,
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY) AS created_at,
    NOW() AS updated_at
FROM numbers;

-- MentorInfo 테이블에 100만 명의 멘토 정보 삽입 --
INSERT INTO mentor_info (member_id, field, job, career, description, total_rating, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT /*+ SET_VAR(cte_max_recursion_depth = 1000000) */
    1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1000000
)
SELECT
    n AS member_id,  -- 1~100만 번의 멘토 회원 ID 사용
    CASE MOD(n, 4)   -- Field를 4가지로 랜덤 배정
        WHEN 0 THEN 'IT'
        WHEN 1 THEN 'INVESTMENT'
        WHEN 2 THEN 'FITNESS'
        WHEN 3 THEN 'STUDY'
    END AS field,
    CONCAT('Mentor Job ', n) AS job,  -- 직업 설정
    FLOOR(RAND() * 20) + 1 AS career,  -- 1~20년 경력 랜덤 설정
    CONCAT('This is mentor ', n) AS description,  -- 설명 설정
    0 As total_rating,
    NOW() AS created_at,  -- 현재 시간
    NOW() AS updated_at   -- 현재 시간
FROM numbers
WHERE n <= 1000000;  -- 멘토 100만 명만 선택

-- Mate 테이블에 일반 유저 1명당 평균 3명의 멘토를 할당 (총 150만 개 관계 생성)
INSERT INTO mate (mentor_id, mentee_id, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT /*+ SET_VAR(cte_max_recursion_depth = 1500000) */
    1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1500000
)
SELECT
    (1 + MOD(n, 1000000)) AS mentor_id,  -- 1~100만 사이 랜덤 멘토 선택
    (1000000 + (1 + FLOOR(n / 3))) AS mentee_id,  -- 1000001~1500000 사이 일반 사용자 선택
    NOW() AS created_at,
    NOW() AS updated_at
FROM numbers
WHERE n <= 1500000;


-- Review 테이블에 150만 개의 멘토 리뷰 생성
INSERT INTO review (info_id, mentee_id, mentor_id, content, rating, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT /*+ SET_VAR(cte_max_recursion_depth = 1500000) */
    1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1500000
)
SELECT
    m.mentor_id AS info_id,   -- 멘토 정보 ID (멘토 ID와 동일)
    m.mentee_id AS mentee_id, -- 리뷰 작성자 (멘티 ID)
    m.mentor_id AS mentor_id, -- 리뷰 대상 (멘토 ID)
    CONCAT('This is a review ', n) AS content, -- 리뷰 내용
    FLOOR(RAND() * 6) AS rating, -- 0~5점 랜덤 점수
    NOW() AS created_at,
    NOW() AS updated_at
FROM mate m
JOIN numbers n ON m.id = n.n
WHERE n <= 1500000;


-- 50만 개 데이터 삽입
INSERT INTO todo (member_id, category, date, created_at, updated_at)
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
INSERT INTO todo_task (todo_id, task, is_done, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT 1 as n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1500000
)
SELECT
    1 + MOD(n, 500000), -- todo_id (1~50만 중 랜덤 배정)
    CONCAT('Task ', n, ' for Todo ', MOD(n, 500000)), -- task 내용
    FALSE -- isDone (초기값 false)
    NOW(),
    NOW()
FROM numbers;


-- Review 테이블의 rating 합산 값을 MentorInfo 테이블의 total_rating으로 업데이트
UPDATE mentor_info mi
JOIN (
    SELECT mentor_id, SUM(rating) AS total_rating_sum
    FROM review
    GROUP BY mentor_id
) r ON mi.member_id = r.mentor_id
SET mi.total_rating = r.total_rating_sum;