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
import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.domain.member.repository.MentorInfoRepository;
import dayou.lifemate.domain.member.service.client.BookingClient;
import dayou.lifemate.domain.member.service.client.MenteeInterestClient;
import dayou.lifemate.domain.member.service.client.RankingClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MentorInfoService {

	private final MentorInfoRepository mentorRepo;
	private final MemberRepository memberRepo;
	private final MenteeInterestClient interestClient;
	private final BookingClient bookingClient;
	private final RankingClient rankingClient;

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

	public MentorDetailResponseDto getMentor(String email, Long mentorInfoId) throws InterruptedException {
		Member mentee = memberRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("회원을 찾지 못했습니다."));
		MentorInfo mentorInfo = mentorRepo.findById(mentorInfoId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

		// 조회 멘토와 관계 형성 가능 여부
		bookingClient.isAsyncAvailable(mentorInfo);
		rankingClient.getAsyncRanking(mentorInfo);
		// 사용자 응답과 관련 없는 코드 -> 비동기적 실행으로 변경
		interestClient.asyncInterest(mentee, mentorInfo);

		return MentorDetailResponseDto.builder()
			.info(mentorInfo)
			.build();
	}
}
