package dayou.lifemate.domain.member.service.client;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.domain.member.repository.MentorInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingClient {
	private final MentorInfoRepository mentorInfoRepo;

	@Async("asyncExecutor")
	public Integer getAsyncRanking(MentorInfo mentorInfo) throws InterruptedException {
		log.info("랭킹 조회 비동기적 실행------------------------ ");
		Thread.sleep(1);
		return mentorInfoRepo.countByFieldAndTotalRatingGreaterThan(mentorInfo.getField(), mentorInfo.getTotalRating())
			+ 1;
	}

	public Integer getSyncRanking(MentorInfo mentorInfo) throws InterruptedException {
		log.info("랭킹 조회 동기적 실행------------------------ ");
		Thread.sleep(1);
		return mentorInfoRepo.countByFieldAndTotalRatingGreaterThan(mentorInfo.getField(), mentorInfo.getTotalRating())
			+ 1;
	}
}
