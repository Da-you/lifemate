package dayou.lifemate.domain.lecture.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dayou.lifemate.domain.lecture.dto.LectureResponseDto;
import dayou.lifemate.domain.lecture.service.LectureService;
import dayou.lifemate.domain.member.service.MemberLoginService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lectures/v1")
@RequiredArgsConstructor
public class LectureApiController {

	private final LectureService lectureService;
	private final MemberLoginService loginService;

	@PostMapping
	public ResponseEntity<LectureResponseDto> createLecture(@AuthenticationPrincipal User user) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(lectureService.createLecture(email));
	}

	@GetMapping("/{id}")
	public ResponseEntity<LectureResponseDto> getLecture(@PathVariable(name = "id") Long lectureId) {
		return ResponseEntity.ok(lectureService.getLecture(lectureId));
	}
}
