package dayou.lifemate.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	ROLE_USER("일반_사용자"),
	ROLE_MENTOR("멘토_사용자"),
	ROLE_ADMIN("관리자");

	private final String name;
}
