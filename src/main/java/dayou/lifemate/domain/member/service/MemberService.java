package dayou.lifemate.domain.member.service;

import static dayou.lifemate.domain.member.enums.Role.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.dto.request.MemberRequestDto;
import dayou.lifemate.domain.member.dto.response.MemberResponseDto;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepo;
	private final PasswordEncoder encoder;

	@Transactional
	public MemberResponseDto join(MemberRequestDto req) {
		Member member = Member.builder()
			.email(req.getEmail())
			.password(encoder.encode(req.getPassword()))
			.nickname(req.getNickname())
			.role(ROLE_USER)
			.build();
		log.info(member.getPassword());
		memberRepo.save(member);

		return MemberResponseDto.builder()
			.member(member)
			.build();
	}

	@Transactional(readOnly = true)
	public MemberResponseDto getMyInfo(String email) {
		Member member = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

		return MemberResponseDto.builder().member(member).build();
	}
}
