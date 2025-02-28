package dayou.lifemate.domain.review.dto.response;

import dayou.lifemate.domain.member.enums.Field;
import dayou.lifemate.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewWithMentorInfoResponseDto {

	private Long mentorId;

	private Long mentorInfoId;

	private Field field;

	private String job;

	private int career;

	private Long reviewId;

	private String content;

	private int rating;

	@Builder
	public ReviewWithMentorInfoResponseDto(Review review) {
		this.mentorId = review.getMentorId();
		this.mentorInfoId = review.getInfo().getId();
		this.field = review.getInfo().getField();
		this.job = review.getInfo().getJob();
		this.career = review.getInfo().getCareer();
		this.reviewId = review.getId();
		this.content = review.getContent();
		this.rating = review.getRating();
	}
}
