package dayou.lifemate.domain.member.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.dto.response.MateResponseDto;
import dayou.lifemate.domain.member.dto.response.MateResponseDto.MateListResponseDto;
import dayou.lifemate.domain.member.entity.Mate;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.Mentor;
import dayou.lifemate.domain.member.repository.MateRepository;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.domain.member.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MateService {
	private final MemberRepository memberRepo;
	private final MentorRepository mentorRepo;
	private final MateRepository mateRepo;

	@Transactional
	public MateResponseDto mate(String email, Long memberId) {
		Member mentee = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

		Member mentor = memberRepo.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

		if (mateRepo.existsByMenteeAndMentor(mentee, mentor)) {
			throw new IllegalArgumentException("이미 Mate 관계에 있습니다.");
		}

		Mate mate = Mate.builder()
			.mentee(mentee)
			.mentor(mentor)
			.build();

		mateRepo.save(mate);

		return MateResponseDto.builder()
			.mate(mate)
			.build();
	}

	@Transactional
	public void deleteMate(String email, Long mateId) {
		Member mentee = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));


		if (mateRepo.findByMenteeAndId(mentee, mateId) == null) {
			throw new IllegalArgumentException("mate 관계가 아닙니다.");
		} else {
			mateRepo.deleteById(mateId);
		}
	}

	@Transactional(readOnly = true)
	public List<MateListResponseDto> getMyMateList(String email) {
		Member mentee = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

		List<MateListResponseDto> res = new ArrayList<>();
		List<Mate> mates = mateRepo.findAllByMentee(mentee);
		for (Mate mate : mates) {
			Mentor mentor = mentorRepo.findByMember(mate.getMentor());
			res.add(
				MateListResponseDto.builder()
					.mateId(mate.getId())
					.mentor(mentor)
					.build()
			);
		}
		return res;
	}
}
