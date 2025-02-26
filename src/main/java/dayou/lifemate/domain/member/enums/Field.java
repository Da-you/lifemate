package dayou.lifemate.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Field {
	IT("IT"),
	INVESTMENT("경제/투자"),
	FITNESS("운동"),
	STUDY("공부");
	private final String name;
}
