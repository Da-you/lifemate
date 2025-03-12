package dayou.lifemate.domain.lecture.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.lecture.entity.Lecture;
import dayou.lifemate.domain.lecture.entity.LectureParticipant;
import dayou.lifemate.domain.lecture.repository.LectureParticipantRepository;
import dayou.lifemate.domain.lecture.repository.LectureRepository;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureJoinService {

	private final LectureRepository lectureRepo;
	private final MemberRepository memberRepo;
	private final LectureParticipantRepository participantRepo;

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

	// 낙관락 사용
	@Transactional
	public void joinLectureUseOptimistic(String email, Long lectureId) {
		Lecture lecture = lectureRepo.findByIdUseOpimistic(lectureId).orElseThrow(
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

		LectureParticipant participant = LectureParticipant.builder()
			.lecture(lecture)
			.member(member)
			.build();
		participantRepo.save(participant);
		lecture.increaseParticipants();
	}
}
