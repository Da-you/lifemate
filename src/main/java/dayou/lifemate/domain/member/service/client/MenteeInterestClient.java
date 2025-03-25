package dayou.lifemate.domain.member.service.client;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.MenteeInterest;
import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.domain.member.repository.MenteeInterestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenteeInterestClient {

	private final MenteeInterestRepository menteeInterestRepo;

	@Transactional
	@Async("interestExecutor")
	public void asyncInterest(Member mentee, MentorInfo mentorInfo) throws InterruptedException {
		Thread.sleep(1); // 외부 api 환경을 구성하기 위해 딜레이 설정
		log.info("관심사 저장 비동기적실행--");
		MenteeInterest interest = menteeInterestRepo.findByMenteeAndField(mentee, mentorInfo.getField());
		if (interest != null) {
			// 관심 목록에 관련된 멘토 리스트 추가
			log.info("카운트 증가");
			interest.updateViewCount();
		} else {
			MenteeInterest menteeInterest = MenteeInterest.builder()
				.mentee(mentee)
				.field(mentorInfo.getField())
				.build();
			menteeInterestRepo.save(menteeInterest);
		}
	}

	@Transactional
	public void syncInterest(Member mentee, MentorInfo mentorInfo) throws InterruptedException {
		Thread.sleep(1); // 외부 api 환경을 구성하기 위해 딜레이 설정
		log.info("관심사 저장 동기 실행--");
		MenteeInterest interest = menteeInterestRepo.findByMenteeAndField(mentee, mentorInfo.getField());
		if (interest != null) {
			// 관심 목록에 관련된 멘토 리스트 추가
			log.info("카운트 증가");
			interest.updateViewCount();
		} else {
			MenteeInterest menteeInterest = MenteeInterest.builder()
				.mentee(mentee)
				.field(mentorInfo.getField())
				.build();
			menteeInterestRepo.save(menteeInterest);
		}
	}
}
