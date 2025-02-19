package dayou.lifemate.domain.member.service;

import org.springframework.stereotype.Service;

import dayou.lifemate.domain.member.dto.MemberRequestDto;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

	private final MemberRepository memberRepo;

	public void login(MemberRequestDto req) {
		if (!memberRepo.existsByEmail(req.getEmail())) {
			throw new IllegalArgumentException("존재하지 않는 이메일 입니다.");
		}
		Member member = memberRepo.findByEmailAndPassword(req.getEmail(),req.getPassword());
	}
}
