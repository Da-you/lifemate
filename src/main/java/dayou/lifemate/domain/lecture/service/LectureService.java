package dayou.lifemate.domain.lecture.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.lecture.dto.LectureResponseDto;
import dayou.lifemate.domain.lecture.entity.Lecture;
import dayou.lifemate.domain.lecture.repository.LectureRepository;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.enums.Role;
import dayou.lifemate.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureService {

	private final LectureRepository lectureRepo;
	private final MemberRepository memberRepo;

	@Transactional
	public LectureResponseDto createLecture(String email) {
		Member mentor = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));
		if (mentor.getRole() != Role.ROLE_MENTOR) {
			throw new IllegalArgumentException("멘토 사용자가 아니면 할 수 없는 작업입니다.");
		}

		Lecture lecture = Lecture.builder()
			.title("title")
			.description("desc")
			.eventDate(LocalDateTime.now())
			.maxParticipants(20)
			.mentor(mentor)
			.build();

		lectureRepo.save(lecture);
		return LectureResponseDto.builder()
			.lecture(lecture)
			.build();
	}

	@Transactional(readOnly = true)
	public LectureResponseDto getLecture(Long lectureId) {
		Lecture lecture = lectureRepo.findById(lectureId).orElseThrow(
			() -> new EntityNotFoundException("존재 하지 않는 강연 입니다."));
		return LectureResponseDto.builder()
			.lecture(lecture)
			.build();
	}
}
