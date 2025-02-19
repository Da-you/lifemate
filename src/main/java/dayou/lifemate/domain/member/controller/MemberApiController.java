package dayou.lifemate.domain.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dayou.lifemate.domain.member.dto.MemberRequestDto;
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
	public String signup(@RequestBody MemberRequestDto req) {
		memberService.join(req);
		return "회원가입 완료";
	}

	@PostMapping("/login")
	public String login(@RequestBody MemberRequestDto req) {
		loginService.login(req);
		return "로그인 완료";
	}
}
