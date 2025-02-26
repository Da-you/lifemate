package dayou.lifemate.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.dto.request.MentorRequestDto;
import dayou.lifemate.domain.member.dto.response.MentorResponseDto;
import dayou.lifemate.domain.member.dto.response.MentorResponseDto.MentorListResponseDto;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.Mentor;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.domain.member.repository.MentorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MentorService {

	private final MentorRepository mentorRepo;
	private final MemberRepository memberRepo;

	@Transactional
	public MentorResponseDto register(String email, MentorRequestDto req) {
		Member member = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 유저 입니다."));

		if (mentorRepo.existsByMember(member)) {
			throw new IllegalArgumentException("이미 멘토로 등록된 유저 입니다.");
		}

		Mentor mentor = Mentor.builder()
			.member(member)
			.field(req.getField())
			.job(req.getJob())
			.career(req.getCareer())
			.description(req.getDescription())
			.build();
		mentorRepo.save(mentor);
		member.registerMentor();

		return MentorResponseDto.builder()
			.mentor(mentor)
			.build();
	}

	@Transactional
	public MentorResponseDto updateInfo(String email, MentorRequestDto req) {
		Member member = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 유저 입니다."));
		Mentor mentor = mentorRepo.findByMember(member);
		mentor.updateInfo(req.getField(), req.getJob(), req.getCareer(), req.getDescription());

		return MentorResponseDto.builder()
			.mentor(mentor)
			.build();
	}

	@Transactional(readOnly = true)
	public Page<MentorListResponseDto> getMentorList(Pageable pageable) {
		Page<Mentor> mentors = mentorRepo.findAll(pageable);
		// 검색 키워드로는 경력(범위), 직무(where)
		return mentors.map(MentorListResponseDto::new);
	}

	@Transactional(readOnly = true)
	public MentorResponseDto getMentor(Long mentorId) {
		Mentor mentor = mentorRepo.findById(mentorId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

		return MentorResponseDto.builder()
			.mentor(mentor)
			.build();
	}
}
