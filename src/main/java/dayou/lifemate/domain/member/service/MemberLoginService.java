package dayou.lifemate.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.dto.MemberRequestDto;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

	private final MemberRepository memberRepo;
	private final PasswordEncoder encoder;
	private final JwtProvider jwtProvider;

	@Transactional
	public String login(MemberRequestDto req) {
		if (!memberRepo.existsByEmail(req.getEmail())) {
			throw new IllegalArgumentException("존재하지 않는 이메일 입니다.");
		}
		Member member = memberRepo.findByEmailAndPassword(req.getEmail(), encoder.encode(req.getPassword()));
		if (member == null) {
			throw new IllegalArgumentException("입력하신 정보를 확인해주세요.");
		}
		return jwtProvider.createToken(member.getEmail());
	}
}
