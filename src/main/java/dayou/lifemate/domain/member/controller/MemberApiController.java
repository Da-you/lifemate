package dayou.lifemate.domain.member.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dayou.lifemate.domain.member.dto.request.MemberLoginRequestDto;
import dayou.lifemate.domain.member.dto.request.MemberRequestDto;
import dayou.lifemate.domain.member.dto.request.MentorRequestDto;
import dayou.lifemate.domain.member.dto.response.MateResponseDto;
import dayou.lifemate.domain.member.dto.response.MateResponseDto.MateListResponseDto;
import dayou.lifemate.domain.member.dto.response.MemberLoginResponseDto;
import dayou.lifemate.domain.member.dto.response.MemberResponseDto;
import dayou.lifemate.domain.member.dto.response.MentorDetailResponseDto;
import dayou.lifemate.domain.member.dto.response.MentorDetailResponseDto.MenterListResponseDto;
import dayou.lifemate.domain.member.dto.response.MentorResponseDto;
import dayou.lifemate.domain.member.service.MateService;
import dayou.lifemate.domain.member.service.MemberLoginService;
import dayou.lifemate.domain.member.service.MemberService;
import dayou.lifemate.domain.member.service.MentorInfoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("members/v1")
public class MemberApiController {

	private final MemberService memberService;
	private final MemberLoginService loginService;
	private final MentorInfoService mentorInfoService;
	private final MateService mateService;

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
		return ResponseEntity.ok(mentorInfoService.register(email, req));
	}

	@PatchMapping("/mentor-info")
	public ResponseEntity<MentorResponseDto> updateMentorInfo(@AuthenticationPrincipal User user,
		@RequestBody MentorRequestDto req) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(mentorInfoService.updateInfo(email, req));
	}

	@GetMapping("/mentor")
	public ResponseEntity<Page<MenterListResponseDto>> getMentorList(Pageable pageable) {
		return ResponseEntity.ok(mentorInfoService.getMentorList(pageable));
	}

	// 멘토 entity pk로 검색
	@PostMapping("/mentor/{id}")
	public ResponseEntity<MentorDetailResponseDto> getMentor(@AuthenticationPrincipal User user,
		@PathVariable(name = "id") Long mentorInfoId) throws InterruptedException {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(mentorInfoService.getMentor(email, mentorInfoId));
	}

	@PostMapping("/mate/{id}")
	public ResponseEntity<MateResponseDto> mate(@AuthenticationPrincipal User user,
		@PathVariable(name = "id") Long memberId) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(mateService.mate(email, memberId));
	}

	@DeleteMapping("/mate/{id}")
	public void deleteMate(@AuthenticationPrincipal User user, @PathVariable(name = "id") Long mateId) {
		String email = loginService.getCurrentMember(user.getUsername());
		mateService.deleteMate(email, mateId);
	}

	@GetMapping("/mate/list")
	public ResponseEntity<List<MateListResponseDto>> getMyMateList(@AuthenticationPrincipal User user) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(mateService.getMyMateList(email));
	}
}
