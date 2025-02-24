package dayou.lifemate.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dayou.lifemate.domain.member.dto.MemberLoginRequestDto;
import dayou.lifemate.domain.member.dto.MemberLoginResponseDto;
import dayou.lifemate.domain.member.dto.MemberRequestDto;
import dayou.lifemate.domain.member.dto.MemberResponseDto;
import dayou.lifemate.domain.member.service.MemberLoginService;
import dayou.lifemate.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("members/v1")
public class MemberApiController {

	private final MemberService memberService;
	private final MemberLoginService loginService;

	@PostMapping("/join")
	public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto req) {
		return ResponseEntity.ok(memberService.join(req));
	}

	@PostMapping("/login")
	public ResponseEntity<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto req) {
		return ResponseEntity.ok(loginService.login(req));
	}
}
