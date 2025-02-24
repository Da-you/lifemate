package dayou.lifemate.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.dto.MemberLoginRequestDto;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberLoginService {

	private final MemberRepository memberRepo;
	private final PasswordEncoder encoder;
	private final JwtProvider jwtProvider;

	@Transactional
	public String login(MemberLoginRequestDto req) {
		Member member = memberRepo.findByEmail(req.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));
		String password = member.getPassword();
		log.info(password);
		if (!encoder.matches(req.getPassword(), password)){
			throw new IllegalArgumentException("비밀번호가 일치 아지 않습니다.");
		}

		return jwtProvider.createToken(member.getEmail());
	}
}
