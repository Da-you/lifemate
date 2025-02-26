package dayou.lifemate.domain.member.dto.request;

import dayou.lifemate.domain.member.enums.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MentorRequestDto {

	private Field field;

	private String job;

	private int career;

	private String description;

}

