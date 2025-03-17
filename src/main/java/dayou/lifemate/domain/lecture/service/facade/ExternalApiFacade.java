package dayou.lifemate.domain.lecture.service.facade;

import org.springframework.context.ApplicationEventPublisher;

import dayou.lifemate.domain.lecture.dto.ExternalPayResponseDto;
import dayou.lifemate.domain.lecture.entity.LectureParticipant;
import dayou.lifemate.domain.lecture.external.ExternalPayApi;
import dayou.lifemate.domain.lecture.service.LectureJoinService;
import dayou.lifemate.domain.lecture.service.event.LectureJoinCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ExternalApiFacade {

	private final LectureJoinService joinService;
	private final ExternalPayApi externaleApi;
	private final ApplicationEventPublisher eventPublisher;

	public void joinLecture(String email, Long lectureId) {

		// 1. DB 트랜잭션 호출
		LectureParticipant participant = joinService.joinLectureReturnEntity(email, lectureId);

		// 2. 외부 api를 호출 결제
		ExternalPayResponseDto res = externaleApi.registerParticipant(lectureId, email);

		if (!res.isSuccess()) {
			// 외부 API 실패 시 보상 트랜잭션 또는 알림
			log.info("외부 API 호출 실패. 강연: {}, 회원: {}", lectureId, email);
		}
		// 2-1 외부 api 응답으로 참가자 정보 업데이트
		joinService.updateExternalId(participant, res.getExternalId());

		// 3. 알림 발송
		// try {
		// 	messageApi.sendLectureJoinMessage(lecture.getTitle(), email, res.getExternalId());
		// }catch (Exception e){
		// 	log.info("알림 실패",e);
		// 	// 알림 발송 실패는 api에 영향을 줘서는 안됨
		// }
		eventPublisher.publishEvent(
			new LectureJoinCompletedEvent(lectureId, participant.getLecture().getTitle(), participant.getMember()
				.getEmail()));
	}
}
