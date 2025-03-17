package dayou.lifemate.domain.lecture.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.lecture.dto.ExternalPayResponseDto;
import dayou.lifemate.domain.lecture.entity.Lecture;
import dayou.lifemate.domain.lecture.entity.LectureParticipant;
import dayou.lifemate.domain.lecture.external.ExternalPayApi;
import dayou.lifemate.domain.lecture.external.SlackMessageApi;
import dayou.lifemate.domain.lecture.repository.LectureParticipantRepository;
import dayou.lifemate.domain.lecture.repository.LectureRepository;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureJoinService {
	/**
	 *  발생 가능 문제
	 *  1. 트랜잭션이 너무 커서 성능 저하 발생 > 트랜잭션 범위 줄이기
	 *  2. 외부 API 장애 시 전체 트랜잭션 롤백
	 *  3. 알림 전송 실패 시에도 트랜잭션 롤백
	 *  4.
	 */
	private final LectureRepository lectureRepo;
	private final MemberRepository memberRepo;
	private final LectureParticipantRepository participantRepo;
	private final ExternalPayApi externaleApi;
	private final SlackMessageApi messageApi;

	// 동시성 문제 발생 코드
	@Transactional
	public void joinLecture(String email, Long lectureId) {
		Lecture lecture = lectureRepo.findById(lectureId).orElseThrow(
			() -> new EntityNotFoundException("강연을 찾을 수 없습니다."));

		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

		LectureParticipant participant = LectureParticipant.builder()
			.lecture(lecture)
			.member(member)
			.build();
		participantRepo.save(participant);
		lecture.increaseParticipants();
	}

	@Transactional
	public LectureParticipant joinLectureReturnEntity(String email, Long lectureId) {
		Lecture lecture = lectureRepo.findById(lectureId).orElseThrow(
			() -> new EntityNotFoundException("강연을 찾을 수 없습니다."));

		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

		lecture.increaseParticipants();

		LectureParticipant participant = LectureParticipant.builder()
			.lecture(lecture)
			.member(member)
			.build();
		participantRepo.save(participant);
		return participant;
	}

	// 낙관락 사용
	@Transactional
	public void joinLectureUseOptimistic(String email, Long lectureId) {
		Lecture lecture = lectureRepo.findByIdUseOpimistic(lectureId).orElseThrow(
			() -> new EntityNotFoundException("강연을 찾을 수 없습니다."));

		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
		lecture.increaseParticipants();

		// 외부 결제 API 사용을 가정
		// 1. 외부 api를 호출해 결제를 하고 이벤트 참가 처리
		ExternalPayResponseDto res = externaleApi.registerParticipant(lectureId, email);

		if (!res.isSuccess()) {
			throw new RuntimeException("외부 API 호출 실패: " + res.getErrorMessage());
		}
		LectureParticipant participant = LectureParticipant.builder()
			.lecture(lecture)
			.member(member)
			.build();
		participantRepo.save(participant);
		//슬랙 발송
		messageApi.sendLectureJoinMessage(lecture.getTitle(), email, res.getExternalId());
	}

	// 비관락 사용
	@Transactional
	public void joinLectureUsePessmistic(String email, Long lectureId) {
		Lecture lecture = lectureRepo.findByIdUsePessimistic(lectureId).orElseThrow(
			() -> new EntityNotFoundException("강연을 찾을 수 없습니다."));

		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

		LectureParticipant participant = LectureParticipant.builder()
			.lecture(lecture)
			.member(member)
			.build();
		participantRepo.save(participant);
		lecture.increaseParticipants();
	}

	// 네임드 락 사용
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void joinLectureUseNamed(String email, Long lectureId) {
		Lecture lecture = lectureRepo.findById(lectureId).orElseThrow(
			() -> new EntityNotFoundException("강연을 찾을 수 없습니다."));

		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
		// 참가자 수 증가
		lecture.increaseParticipants();
		// 2. 참가자 정보를 저장
		LectureParticipant participant = LectureParticipant.builder()
			.lecture(lecture)
			.member(member)
			.build();
		participantRepo.save(participant);
	}

	// 참가자 정보에 외부 api 정보 저장
	@Transactional
	public void updateExternalId(LectureParticipant participant, String externalId) {
		participant.updatedExternalId(externalId);
	}
}
