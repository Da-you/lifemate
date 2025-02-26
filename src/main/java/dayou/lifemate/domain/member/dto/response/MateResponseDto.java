package dayou.lifemate.domain.member.dto.response;

import dayou.lifemate.domain.member.entity.Mate;
import dayou.lifemate.domain.member.entity.Mentor;
import dayou.lifemate.domain.member.enums.Field;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MateResponseDto {
	private String mentee;
	private String mentor;

	@Builder
	public MateResponseDto(Mate mate) {
		this.mentee = mate.getMentee().getNickname();
		this.mentor = mate.getMentor().getNickname();
	}

	@Getter
	@NoArgsConstructor
	public static class MateListResponseDto {
		private Long mateId;

		private String nickname;

		private Field field;

		private String job;

		private int career;

		@Builder
		public MateListResponseDto(Long mateId, Mentor mentor) {
			this.mateId = mateId;
			this.nickname = mentor.getMember().getNickname();
			this.field = mentor.getField();
			this.job = mentor.getJob();
			this.career = mentor.getCareer();
		}
	}
}
