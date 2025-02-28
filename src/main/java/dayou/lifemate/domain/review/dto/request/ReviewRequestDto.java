package dayou.lifemate.domain.review.dto.request;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

	private String content;
	@Range(min = 0, max = 5)
	private int rating;
}
