package dayou.lifemate.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.dto.request.MentorRequestDto;
import dayou.lifemate.domain.member.dto.response.MentorDetailResponseDto;
import dayou.lifemate.domain.member.dto.response.MentorDetailResponseDto.MenterListResponseDto;
import dayou.lifemate.domain.member.dto.response.MentorResponseDto;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.MenteeInterest;
import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.domain.member.repository.MenteeInterestRepository;
import dayou.lifemate.domain.member.repository.MentorInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MentorInfoService {

	private final MentorInfoRepository mentorRepo;
	private final MemberRepository memberRepo;
	private final MenteeInterestRepository menteeInterestRepo;

	@Transactional
	public MentorResponseDto register(String email, MentorRequestDto req) {
		Member member = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 유저 입니다."));

		if (mentorRepo.existsByMember(member)) {
			throw new IllegalArgumentException("이미 멘토로 등록된 유저 입니다.");
		}

		MentorInfo mentorInfo = MentorInfo.builder()
			.member(member)
			.field(req.getField())
			.job(req.getJob())
			.career(req.getCareer())
			.description(req.getDescription())
			.build();
		mentorRepo.save(mentorInfo);
		member.registerMentor();

		return MentorResponseDto.builder()
			.info(mentorInfo)
			.build();
	}

	@Transactional
	public MentorResponseDto updateInfo(String email, MentorRequestDto req) {
		Member member = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 유저 입니다."));
		MentorInfo mentorInfo = mentorRepo.findByMember(member);
		mentorInfo.updateInfo(req.getField(), req.getJob(), req.getCareer(), req.getDescription());

		return MentorResponseDto.builder()
			.info(mentorInfo)
			.build();
	}

	@Transactional(readOnly = true)
	public Page<MenterListResponseDto> getMentorList(Pageable pageable) {
		Page<MentorInfo> mentors = mentorRepo.findAll(pageable);
		// 검색 키워드로는 경력(범위), 직무(where)

		return mentors.map(MenterListResponseDto::new);
	}

	@Transactional
	public MentorDetailResponseDto getMentor(String email, Long mentorId) {
		Member mentee = memberRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("회원을 찾지 못했습니다."));
		MentorInfo mentorInfo = mentorRepo.findById(mentorId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
		MenteeInterest interest = menteeInterestRepo.findByMenteeAndField(mentee, mentorInfo.getField());
		if (interest != null) {
			// 관심 목록에 관련된 멘토 리스트 추가
			interest.updateViewCount();
		}
		MenteeInterest menteeInterest = MenteeInterest.builder()
			.mentee(mentee)
			.field(mentorInfo.getField())
			.build();
		menteeInterestRepo.save(menteeInterest);

		return MentorDetailResponseDto.builder()
			.info(mentorInfo)
			.build();
	}
}
