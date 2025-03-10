LifeMate
==

# 개요

- 투두리스트와 멘토/멘티 연결

---

# 트러블 슈팅

## 문제 상황: 150만개의 투두 리스트 조회 시 쿼리 카운터로 api 실행 시 42개의 쿼리 발생하며 10.45s 수행 시간이 필요

- 수행 API: http://localhost:8080/todos/v1

### 문제 파악 및 원인 분석

- K6 시나리오 테스트 수행 시 api 수행 시간이 3초가 넘어 WARN 로그를 발생
- or
- 포스트맨으로 테스트 수행시 api 수행시간이 10.45s가 나오는 것을 발견
- 발생 쿼리를 확인해 N + 1 문제 확인
- 쿼리 카운터를 생성해 api 실행 시 발생 쿼리의 수를 확인
- DBMS 를 이용해 쿼리 플랜을 확인해 풀 스캔이 진행 되는 것을 확인

정리
N + 1, 인덱스 없이 테이블 풀 스캔으로 인한 문제 발생으로 인식

### 쿼리 플랜

- temporary table, filter sort 사용을 확인
    - 메모리에서 임시 테이블(temporary table) 생성해 정렬 작업이 진행되어 성능 저하가 될것이라 추측
- todo의 하위 관계인 todo_task 탐색 진행 시 hash join 발생(인덱스 사용이 없어 Full scan 발생)

## 개선 방법

우선 적용

- 페이징 처리 (이미 적용)
- FetchType.LAZY 적용(이미 적용)
    - 실제 데이터 조회시에는 똑같이 N + 1 문제 발생
        - 프록시 객체가 아닌 실제 객체를 조회시 결국 N +1 문제가 발생
    - @EntityGraph 적용

> @EntityGraph 를 이용해 발생 쿼리를 42 개-> 22 개 감소


개선 방법

- 최대한 카디널리티를 구분할 수 있는 컬럼을 이용해 복합 인덱스 사용 todo 테이블

> ALTER TABLE todo ADD INDEX idx_category_date_created (category, date, created_at); 을 사용해 인덱스 적용

- 기대 효과
    - 임시 테이블과 정렬 작업 제거
- 발생 효과
    - 10.45s -> 4.59s 개선

남아있는 문제
여전히 todo_task 조회 시 full scan 진행됨

- 단일 인덱스 사용

> ALTER TABLE todo_task ADD INDEX idx_todo_id (todo_id);

- 기대 효과
    - todo_task 에서 발생하는 full scan 제거
- 발생 효과
    - full scan 제거
    - 4.59s -> 816ms 개선

- 추가 개선 Todo 조회 시 member 테이블을 탐색하면서 N +1 문제 발생
    - @EntityGraph 에 추가 적용

- 기대 효과
    - 쿼리 수 감소
    - 수행 시간 감소
- 발생 효과
    - 발생 쿼리 수 22 -> 2 개로 감소
    - 수행 시간 4.59s -> 522ms로 감소

정리

문제 상황

- 150만개의 투두 데이터가 존재
- 지정 날짜와 카테고리를 입력해 해당하는 모든 투두를 가져오는 api 실행
- 포스트맨 수행 시간 10.45s 소요
- 커스텀 쿼리 카운터로 발생 쿼리 파악 42개의 쿼리 발생
- 이미 페이징, FetchType.LAZY는 적용된 상태

원인 파악 과정

- 먼저 로그에서 확인 가능한 쿼리로 쿼리 플랜을 확인
    - 확인 결과 인덱스가 존재하지 않아 todo 테이블 full scan, 정렬을 위한 temporary 테이블에서 정렬 작업 발생
    - todo 테이블의 1:N 관계에서 N측인 todo_tasks 탐색시 todo_id 인덱스가 존재하지 않아 Hash Join 발생
        - Hash Join은 해시 테이블을 메모리에 생성하고 조인시 버퍼를 사용해 Nested Loop Join 보다는 빠르나 역시 성능 저하가 발생함
    - FetchType.LAZY을 적용했으나 N+1 문제 발생
        - 프록시 객체가 아닌 실제 객체를 조회시 결국 N +1 문제가 발생함

해결 과정

- todo 테이블의 full scan 개선을 위해 *카디널리티를 고려해 지정 날짜, 카테고리를 이용해 복합 인덱스*를 생성
- todo_task 테이블에서 발생하는 N + 1 해결을 위해 @EntityGraph 적용

> 1차 개선 후 성능 측정( 발생 쿼리 : 42 -> 22, 수행 시간 10.45s -> 4.59s)

- todo_task 테이블 조회 시 N + 1 문제는 제거했으나 full scan 발생하여 todo_task 의 todo_id 컬럼을 이용해 단일 인덱스를 생성해 적용 
- member 테이블의 N + 1 문제 발생을 확인해 @EntityGraph 추가 적용

> 2차 개선 후 성능 측정( 발생 쿼리 : 22 -> 2, 수행 시간 4.59s -> 522ms)