package dayou.lifemate.domain.review.controller;

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

import dayou.lifemate.domain.member.service.MemberLoginService;
import dayou.lifemate.domain.review.dto.request.ReviewRequestDto;
import dayou.lifemate.domain.review.dto.response.ReviewResponseDto;
import dayou.lifemate.domain.review.dto.response.ReviewWithMentorInfoResponseDto;
import dayou.lifemate.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews/v1")
@RequiredArgsConstructor
public class ReviewApiController {
	private final ReviewService reviewService;
	private final MemberLoginService loginService;

	@PostMapping("/{mate_id}")
	public ResponseEntity<ReviewResponseDto> createReview(@AuthenticationPrincipal User user,
		@PathVariable Long mate_id, @RequestBody ReviewRequestDto req) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(reviewService.createReview(email, mate_id, req));
	}

	@PatchMapping("/{review_id}")
	public ResponseEntity<ReviewResponseDto> updateReview(@AuthenticationPrincipal User user,
		@PathVariable Long review_id, @RequestBody ReviewRequestDto req) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(reviewService.updateReview(email, review_id, req));
	}

	@DeleteMapping("/{review_id}")
	public ResponseEntity<Boolean> deleteReview(@AuthenticationPrincipal User user, @PathVariable Long review_id) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(reviewService.deleteReview(email, review_id));
	}

	@GetMapping
	public ResponseEntity<Page<ReviewWithMentorInfoResponseDto>> getReviewList(Pageable pageable) {
		return ResponseEntity.ok(reviewService.getReviewList(pageable));
	}

}
