package dayou.lifemate.domain.lecture.service.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.lecture.repository.LectureRepository;
import dayou.lifemate.domain.lecture.service.LectureJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NamedLockFacade implements LockFacade {
	private final LectureJoinService joinService;
	private final LectureRepository lectureRepo;

	@Override
	@Transactional // 트랜잭션 범위를 DB 단계에서만 진행
	public void joinLecture(String email, Long lectureId) {
		int maxRetries = 5;
		int retryCount = 0;

		while (retryCount < maxRetries) {
			try {
				lectureRepo.getLock(String.format("lecture_%d", lectureId), 3);
				joinService.joinLectureUseNamed(email, lectureId);
				return;
			} catch (Exception e) {
				retryCount++;
				if (retryCount == maxRetries) {
					throw new RuntimeException("최대 재시도 횟수를 초과했습니다.");
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("인터럽트 발생", ie);
				}
			} finally {
				lectureRepo.releaseLock(String.format("lecture_%d", lectureId));
			}
		}
	}
}
