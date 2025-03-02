package dayou.lifemate.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.dto.request.MemberLoginRequestDto;
import dayou.lifemate.domain.member.dto.response.MemberLoginResponseDto;
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
	public MemberLoginResponseDto login(MemberLoginRequestDto req) {
		Member member = memberRepo.findByEmail(req.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));
		String password = member.getPassword();
		log.info(password);
		// if (!encoder.matches(req.getPassword(), password)) {
		// 	throw new IllegalArgumentException("비밀번호가 일치 하지 않습니다.");
		// }

		return MemberLoginResponseDto.builder()
			.email(member.getEmail())
			.accessToken(jwtProvider.createToken(member.getEmail()))
			.build();
	}

	@Transactional(readOnly = true)
	public String getCurrentMember(String email) {
		Member member = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

		log.info("Found member: {}", member);
		return member.getEmail();
	}
}
