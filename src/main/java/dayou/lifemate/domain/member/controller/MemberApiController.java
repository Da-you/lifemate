package dayou.lifemate.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dayou.lifemate.domain.member.dto.request.MemberLoginRequestDto;
import dayou.lifemate.domain.member.dto.request.MentorRequestDto;
import dayou.lifemate.domain.member.dto.response.MemberLoginResponseDto;
import dayou.lifemate.domain.member.dto.request.MemberRequestDto;
import dayou.lifemate.domain.member.dto.response.MemberResponseDto;
import dayou.lifemate.domain.member.dto.response.MentorResponseDto;
import dayou.lifemate.domain.member.dto.response.MentorResponseDto.MentorListResponseDto;
import dayou.lifemate.domain.member.service.MemberLoginService;
import dayou.lifemate.domain.member.service.MemberService;
import dayou.lifemate.domain.member.service.MentorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("members/v1")
public class MemberApiController {

	private final MemberService memberService;
	private final MemberLoginService loginService;
	private final MentorService mentorService;

	@PostMapping("/join")
	public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto req) {
		return ResponseEntity.ok(memberService.join(req));
	}

	@PostMapping("/login")
	public ResponseEntity<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto req) {
		return ResponseEntity.ok(loginService.login(req));
	}

	@GetMapping("/my-info")
	public ResponseEntity<MemberResponseDto> getMyInfo(@AuthenticationPrincipal User user) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(memberService.getMyInfo(email));
	}

	@PostMapping("/mentor")
	public ResponseEntity<MentorResponseDto> register(@AuthenticationPrincipal User user,
		@RequestBody MentorRequestDto req) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(mentorService.register(email, req));
	}

	@PatchMapping("/mentor-info")
	public ResponseEntity<MentorResponseDto> updateMentorInfo(@AuthenticationPrincipal User user,
		@RequestBody MentorRequestDto req) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(mentorService.register(email, req));
	}

	@GetMapping("/mentor")
	public ResponseEntity<Page<MentorListResponseDto>> getMentorList(Pageable pageable) {
		return ResponseEntity.ok(mentorService.getMentorList(pageable));
	}

	@GetMapping("/mentor/{id}")
	public ResponseEntity<MentorResponseDto> getMentor(@PathVariable Long id) {
		return ResponseEntity.ok(mentorService.getMentor(id));
	}

}
