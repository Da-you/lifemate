package dayou.lifemate.domain.lecture.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExternalPayResponseDto {

	private boolean success;
	private String externalId;
	private Long lectureId;
	private String email;
	private String message;
	private String errorMessage;
}
