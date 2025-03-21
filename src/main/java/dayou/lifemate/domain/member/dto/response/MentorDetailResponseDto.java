package dayou.lifemate.domain.member.dto.response;

import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.domain.member.enums.Field;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MentorDetailResponseDto {

	private Long memberId;

	private Long infoId;

	private String nickname;

	private Field field;

	private String job;

	private int career;

	private String description;

	private long totalMenteeCount;

	private long totalReviewCount;

	private double averageRating;

	// list
	@Builder
	public MentorDetailResponseDto(MentorInfo info) {
		this.memberId = info.getMember().getId();
		this.infoId = info.getId();
		this.nickname = info.getMember().getNickname();
		this.field = info.getField();
		this.job = info.getJob();
		this.career = info.getCareer();
		this.description = info.getDescription();
		this.totalReviewCount = info.getReviews().size();
		this.averageRating = (double)info.getTotalRating() / info.getReviews().size();
		this.totalMenteeCount = info.getMember().getMates().size();
	}

	@Getter
	@NoArgsConstructor
	public static class MenterListResponseDto {
		private Long infoId;
		private String nickname;
		private Field field;
		private double averageRating;

		@Builder
		public MenterListResponseDto(MentorInfo info) {
			this.infoId = info.getId();
			this.nickname = info.getMember().getNickname();
			this.field = info.getField();
			this.averageRating = (double)info.getTotalRating() / info.getReviews().size();
		}
	}

}
