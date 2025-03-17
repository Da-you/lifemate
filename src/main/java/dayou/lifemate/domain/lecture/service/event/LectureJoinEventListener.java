package dayou.lifemate.domain.lecture.service.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import dayou.lifemate.domain.lecture.external.ExternalMessageApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LectureJoinEventListener {
	private final ExternalMessageApi messageApi;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleEventJoinCompleted(LectureJoinCompletedEvent event) {
		try {
			messageApi.sendEventJoinMessage(
				event.getEventName(),
				event.getEmail()
			);
		} catch (Exception e) {
			log.error("알림 발송 실패. eventId={}, eventName={}",
				event.getEventId(),
				event.getEventName(),
				e);
		}
	}
}
