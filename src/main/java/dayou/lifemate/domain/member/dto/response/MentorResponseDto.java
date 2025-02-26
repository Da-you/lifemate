package dayou.lifemate.domain.member.dto.response;

import dayou.lifemate.domain.member.entity.Mentor;
import dayou.lifemate.domain.member.enums.Field;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MentorResponseDto {

	private Long mentorId;

	private String nickname;

	private Field field;

	private String job;

	private int career;

	private String description;

	@Builder
	public MentorResponseDto(Mentor mentor) {
		this.mentorId = mentor.getId();
		this.nickname = mentor.getMember().getNickname();
		this.field = mentor.getField();
		this.job = mentor.getJob();
		this.career = mentor.getCareer();
		this.description = mentor.getDescription();
	}

	@Getter
	@NoArgsConstructor
	public static class MentorListResponseDto {
		private Long mentorId;

		private String nickname;

		private Field field;

		private String job;

		private int career;

		@Builder
		public MentorListResponseDto(Mentor mentor) {
			this.mentorId = mentor.getId();
			this.nickname = mentor.getMember().getNickname();
			this.field = mentor.getField();
			this.job = mentor.getJob();
			this.career = mentor.getCareer();
		}
	}
}
