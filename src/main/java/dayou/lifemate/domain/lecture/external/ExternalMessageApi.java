package dayou.lifemate.domain.lecture.external;

import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
// 슬랙 메시지 발행이 내가 생각한것과 다르게 동작하여 임의 생성
@Slf4j
@Component
public class ExternalMessageApi {

	private static final Random random = new Random();

	public void sendEventJoinMessage(String email, String eventName) {
		try {
			Thread.sleep(random.nextInt(500, 1500));
			log.info("외부 api 알림 발송 완료 - 이벤트: {}", eventName);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("외부 api 알림 발송 중 인터럽트 발생", e);
		}
	}
}
