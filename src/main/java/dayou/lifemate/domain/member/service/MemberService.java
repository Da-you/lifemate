package dayou.lifemate.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.dto.MemberRequestDto;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepo;

	@Transactional
	public void join(MemberRequestDto req) {
		Member member = Member.builder()
			.email(req.getEmail())
			.password(req.getPassword())
			.nickname(req.getNickname())
			.build();
		memberRepo.save(member);
	}
}
