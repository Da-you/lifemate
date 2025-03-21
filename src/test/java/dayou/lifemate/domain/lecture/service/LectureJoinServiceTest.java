package dayou.lifemate.domain.lecture.service;

import static dayou.lifemate.domain.member.enums.Role.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import dayou.lifemate.domain.lecture.entity.Lecture;
import dayou.lifemate.domain.lecture.repository.LectureParticipantRepository;
import dayou.lifemate.domain.lecture.repository.LectureRepository;
import dayou.lifemate.domain.lecture.service.facade.NamedLockFacade;
import dayou.lifemate.domain.lecture.service.facade.OptimisticLockFacade;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class LectureJoinServiceTest {

	@Autowired
	private LectureService lectureService;
	@Autowired
	private LectureJoinService joinService;
	@Autowired
	private LectureRepository lectureRepo;
	@Autowired
	private MemberRepository memberRepo;
	@Autowired
	private LectureParticipantRepository participantRepo;
	@Autowired
	private OptimisticLockFacade optimisticLockFacade;
	@Autowired
	private NamedLockFacade namedLockFacade;

	private Lecture testLecture;
	private Member testMentor;
	private List<Member> testMembers;

	private static final int THREAD_COUNT = 100;

	@BeforeEach
	void setUp() {
		// 기존 데이터 정리
		participantRepo.deleteAllInBatch();
		lectureRepo.deleteAllInBatch();
		memberRepo.deleteAllInBatch();

		// 테스트 용 멘토 생성
		testMentor = Member.builder()
			.email("mentor@test.com")
			.password("password")
			.nickname("testMentor")
			.role(ROLE_MENTOR)
			.build();
		memberRepo.save(testMentor);

		// 테스트용 강연 생성
		testLecture = Lecture.builder()
			.mentor(testMentor)
			.title("테스트 강연")
			.description("동시성 테스트")
			.eventDate(LocalDateTime.now().plusDays(7))
			.maxParticipants(100)
			.build();
		lectureRepo.saveAndFlush(testLecture);

		// 테스트용 회원 100명 생성
		testMembers = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Member member = Member.builder()
				.email("test" + (i + 1) + "@test.com")
				.password("password")
				.nickname("테스터" + (i + 1))
				.role(ROLE_USER)
				.build();
			testMembers.add(member);
		}
		memberRepo.saveAll(testMembers);
		memberRepo.flush();
	}

	@AfterEach
	void cleanup() {
		participantRepo.deleteAllInBatch();
		lectureRepo.deleteAllInBatch();
		memberRepo.deleteAllInBatch();
	}

	@Test
	@DisplayName("트랜잭션만 사용 시 동시성 테스트")
	void transactionDoesNotGuaranteeAtomicity() throws InterruptedException {
		// given
		Lecture lecture = lectureRepo.findAll().get(0);
		List<Member> members = memberRepo.findAll();
		int numberOfThreads = 150;

		// 스레드 풀 크기를 더 크게 설정
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		CountDownLatch startLatch = new CountDownLatch(1); // 추가
		CountDownLatch endLatch = new CountDownLatch(numberOfThreads);

		// when
		for (int i = 0; i < numberOfThreads; i++) {
			final int index = i;
			executorService.submit(() -> {
				try {
					startLatch.await(); // 모든 스레드가 동시에 시작하도록
					joinService.joinLecture(members.get(index).getEmail(), lecture.getId());
				} catch (Exception e) {
					log.error("Error: {}", e.getMessage());
				} finally {
					endLatch.countDown();
				}
			});
		}

		startLatch.countDown(); // 모든 스레드를 동시에 시작
		endLatch.await();
		executorService.shutdown();

		// then
		Lecture updatedLecture = lectureRepo.findById(lecture.getId()).orElseThrow();
		long actualParticipantCount = participantRepo.countByLectureId(lecture.getId());

		System.out.println("강연 최대 참가 가능 인원: " + updatedLecture.getMaxParticipants());
		System.out.println("DB에 저장된 참가자 수: " + updatedLecture.getCurrentParticipants());
		System.out.println("실제 등록된 참가자 수: " + actualParticipantCount);

		// 참가자 수가 최대 인원을 초과했거나
		// DB의 currentParticipants와 실제 등록된 참가자 수가 다른 경우 테스트 성공
		boolean hasInconsistency = updatedLecture.getCurrentParticipants() > updatedLecture.getMaxParticipants() ||
			updatedLecture.getCurrentParticipants() != actualParticipantCount;

		Assert.isTrue(hasInconsistency, "참가자 수가 최대 인원을 초과했거나 DB의 currentParticipants와 실제 등록된 참가자 수가 다른 경우");
	}

	@FunctionalInterface
	interface LectureJoinTask {
		void join(Long lectureId, String email) throws Exception;
	}

	private void executeConcurrentJoins(LectureJoinTask joinTask) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			final String email = testMembers.get(i).getEmail();
			executorService.submit(() -> {
				try {
					joinTask.join(testLecture.getId(), email);
				} catch (Exception e) {
					log.error("이벤트 참가 실패: {}", e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();
	}

	@Test
	@DisplayName("비관적 락으로 100명 동시 참가 테스트")
	void pessimisticLockTest() throws InterruptedException {
		// when
		long startTime = System.currentTimeMillis();
		executeConcurrentJoins((lectureId, email) ->
			joinService.joinLectureUsePessmistic(email, lectureId));
		long executionTime = System.currentTimeMillis() - startTime;

		// then
		Lecture lecture = lectureRepo.findById(testLecture.getId()).orElseThrow();
		log.info("=== 비관적 락 테스트 결과 ===");
		log.info("실행 시간: {}ms", executionTime);
		log.info("최종 참가자 수: {}", lecture.getCurrentParticipants());

		assertThat(lecture.getCurrentParticipants()).isEqualTo(THREAD_COUNT);
	}

	@Test
	@DisplayName("낙관적 락으로 100명 동시 참가 테스트")
	void optimisticLockTest() throws InterruptedException {
		// when
		long startTime = System.currentTimeMillis();
		executeConcurrentJoins((lectureId, email) ->
			optimisticLockFacade.joinLecture(email, lectureId));
		long executionTime = System.currentTimeMillis() - startTime;

		// then
		Lecture lecture = lectureRepo.findById(testLecture.getId()).orElseThrow();
		log.info("=== 낙관적 락 테스트 결과 ===");
		log.info("실행 시간: {}ms", executionTime);
		log.info("최종 참가자 수: {}", lecture.getCurrentParticipants());

		assertThat(lecture.getCurrentParticipants()).isEqualTo(THREAD_COUNT);
	}

	@Test
	@DisplayName("네임드 락으로 100명 동시 참가 테스트")
	void namedLockTest() throws InterruptedException {
		// when
		long startTime = System.currentTimeMillis();
		executeConcurrentJoins((lectureId, email) -> namedLockFacade.joinLecture(email, lectureId));
		long executionTime = System.currentTimeMillis() - startTime;

		// then
		Lecture event = lectureRepo.findById(testLecture.getId()).orElseThrow();
		log.info("=== 네임드 락 테스트 결과 ===");
		log.info("실행 시간: {}ms", executionTime);
		log.info("최종 참가자 수: {}", event.getCurrentParticipants());

		assertThat(event.getCurrentParticipants()).isEqualTo(THREAD_COUNT);
	}

}