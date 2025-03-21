package dayou.lifemate.domain.lecture.service.facade;

import org.springframework.stereotype.Service;

import dayou.lifemate.domain.lecture.service.LectureJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptimisticLockFacade implements LockFacade {

	private final LectureJoinService joinService;

	private static final long RETRY_DELAY_MS = 50;

	@Override
	public void joinLecture(String email, Long lectureId) throws InterruptedException {
		int retryCount = 0;

		while (true) {
			try {
				joinService.joinLectureUseOptimistic(email, lectureId);
				log.info("강연 참가 성공 - lectureId: {}, email: {}, 총 시도횟수: {}",
					lectureId, email, retryCount + 1);
				return;
			} catch (Exception e) {
				retryCount++;
				log.warn("강연 참가 재시도 - lectureId: {}, email: {}, 현재 시도횟수: {}, error: {}",
					lectureId, email, retryCount, e.getMessage());
				Thread.sleep(RETRY_DELAY_MS);
			}
		}
	}
}

