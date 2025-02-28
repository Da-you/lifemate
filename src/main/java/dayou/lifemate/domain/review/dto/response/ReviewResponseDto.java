package dayou.lifemate.domain.review.dto.response;

import dayou.lifemate.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {

	private Long reviewId;
	private String content;
	private int rating;

	@Builder
	public ReviewResponseDto(Review review) {
		this.reviewId = review.getId();
		this.content = review.getContent();
		this.rating = review.getRating();
	}
}
