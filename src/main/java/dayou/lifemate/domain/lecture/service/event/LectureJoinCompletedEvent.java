package dayou.lifemate.domain.lecture.service.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LectureJoinCompletedEvent {
	private final Long eventId;
	private final String eventName;
	private final String email;
}
