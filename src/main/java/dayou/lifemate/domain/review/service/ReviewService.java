package dayou.lifemate.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.entity.Mate;
import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.domain.member.repository.MateRepository;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.domain.member.repository.MentorInfoRepository;
import dayou.lifemate.domain.review.dto.request.ReviewRequestDto;
import dayou.lifemate.domain.review.dto.response.ReviewResponseDto;
import dayou.lifemate.domain.review.dto.response.ReviewWithMentorInfoResponseDto;
import dayou.lifemate.domain.review.entity.Review;
import dayou.lifemate.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final MateRepository mateRepo;
	private final MemberRepository memberRepo;
	private final ReviewRepository reviewRepo;
	private final MentorInfoRepository infoRepo;

	@Transactional
	public ReviewResponseDto createReview(String email, Long mate_id, ReviewRequestDto req) {
		Member mentee = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 유저 입니다."));
		Mate mate = mateRepo.findByMenteeAndId(mentee, mate_id);
		MentorInfo info = infoRepo.findByMember(mate.getMentor());

		Review review = Review.builder()
			.info(info)
			.menteeId(mentee.getId())
			.content(req.getContent())
			.rating(req.getRating())
			.build();
		reviewRepo.save(review);
		info.plusRating(req.getRating());
		return ReviewResponseDto.builder()
			.review(review)
			.build();
	}

	@Transactional
	public ReviewResponseDto updateReview(String email, Long reviewId, ReviewRequestDto req) {
		Member mentee = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 유저 입니다."));
		Review review = reviewRepo.findByIdAndMenteeId(reviewId, mentee.getId());
		if (review == null) {
			throw new IllegalArgumentException("리뷰가 존재하지 않습니다.");
		}
		review.updateReview(req.getContent(), req.getRating());
		return ReviewResponseDto.builder()
			.review(review)
			.build();
	}

	@Transactional
	public Boolean deleteReview(String email, Long reviewId) {
		Member mentee = memberRepo.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 유저 입니다."));
		Review review = reviewRepo.findByIdAndMenteeId(reviewId, mentee.getId());
		if (review == null) {
			throw new IllegalArgumentException("리뷰가 존재하지 않습니다.");
		} else {
			reviewRepo.delete(review);
		}
		return true;
	}

	@Transactional(readOnly = true)
	public Page<ReviewWithMentorInfoResponseDto> getReviewList(Pageable pageable) {
		// mentorId,mentorInfoId, 멘토 닉네임, 필드, job, 경력
		// 내용, 별점
		Page<Review> reviews = reviewRepo.findAll(pageable);
		return reviews.map(ReviewWithMentorInfoResponseDto::new);
	}

}