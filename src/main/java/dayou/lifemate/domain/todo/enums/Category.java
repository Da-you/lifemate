package dayou.lifemate.domain.todo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
	WORK("업무"),
	LIFE("생활"),
	HEALTH("건강"),
	STUDY("자기 개발");

	private final String title;
}
