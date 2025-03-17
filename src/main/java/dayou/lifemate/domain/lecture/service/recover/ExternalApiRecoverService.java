package dayou.lifemate.domain.lecture.service.recover;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dayou.lifemate.domain.lecture.dto.ExternalPayResponseDto;
import dayou.lifemate.domain.lecture.entity.LectureParticipant;
import dayou.lifemate.domain.lecture.external.ExternalPayApi;
import dayou.lifemate.domain.lecture.repository.LectureParticipantRepository;
import dayou.lifemate.domain.lecture.service.LectureJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiRecoverService {
	private final ExternalPayApi payApi;
	private final LectureJoinService joinService;
	private final LectureParticipantRepository participantRepo;

	/**
	 * 5분마다 실행되어 외부 ID가 없는 참가자들을 동기화
	 */
	@Scheduled(fixedDelay = 300000) // 5분
	public void recoverMissingExternalIds() {
		// 1. external_id가 없는 참가자들 조회
		List<LectureParticipant> participantsWithoutExternalId = participantRepo.findByExternalIdIsNullAndCreatedAtBefore(
			LocalDateTime.now()
		);

		log.info("외부 ID 미할당 참가자 발견: {}건", participantsWithoutExternalId.size());

		// 2. 각 참가자별로 외부 시스템 조회 및 업데이트
		for (LectureParticipant participant : participantsWithoutExternalId) {
			syncExternalId(participant);
		}
	}


	/**
	 * 개별 참가자의 외부 ID 동기화
	 */
	public void syncExternalId(LectureParticipant participant) {
		// 1. 외부 시스템에서 참가자 정보 조회 (트랜잭션 밖에서)
		ExternalPayResponseDto response = payApi.getParticipantInfo(
			participant.getLecture().getId(),
			participant.getMember().getEmail()
		);

		if (!response.isSuccess() || response.getExternalId() == null) {
			log.warn("참가자 ID: {}의 외부 시스템 정보 없음 (응답: {})",
				participant.getId(), response);
			return;
		}

		joinService.updateExternalId(participant, response.getExternalId());
	}
}
