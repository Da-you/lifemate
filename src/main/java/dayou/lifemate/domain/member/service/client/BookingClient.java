package dayou.lifemate.domain.member.service.client;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import dayou.lifemate.domain.member.entity.MentorInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingClient {

	@Async("asyncExecutor")
	public Boolean isAsyncAvailable(MentorInfo mentorInfo) throws InterruptedException {
		log.info("멘토링 가능 여부 조회 비동기적 실행------------------------ ");
		Thread.sleep(1);
		return mentorInfo.isAvailable();
	}

	public Boolean isSyncAvailable(MentorInfo mentorInfo) throws InterruptedException {
		log.info("멘토링 가능 여부 조회 비동기적 실행------------------------ ");
		Thread.sleep(1);
		return mentorInfo.isAvailable();
	}
}
