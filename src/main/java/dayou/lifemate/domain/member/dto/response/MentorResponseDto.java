package dayou.lifemate.domain.member.dto.response;

import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.domain.member.enums.Field;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MentorResponseDto {

	private Long infoId;

	private String nickname;

	private Field field;

	private String job;

	private int career;

	private String description;

	@Builder
	public MentorResponseDto(MentorInfo mentorInfo) {
		this.infoId = mentorInfo.getId();
		this.nickname = mentorInfo.getMember().getNickname();
		this.field = mentorInfo.getField();
		this.job = mentorInfo.getJob();
		this.career = mentorInfo.getCareer();
		this.description = mentorInfo.getDescription();
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
		public MentorListResponseDto(MentorInfo mentorInfo) {
			this.mentorId = mentorInfo.getId();
			this.nickname = mentorInfo.getMember().getNickname();
			this.field = mentorInfo.getField();
			this.job = mentorInfo.getJob();
			this.career = mentorInfo.getCareer();
		}
	}
}
