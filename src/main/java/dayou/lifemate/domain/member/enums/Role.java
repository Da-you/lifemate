package dayou.lifemate.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	ROLE_USER("일반_사용자"),
	ROLE_LEADER("관리자"),
	ROLE_ADMIN("리더_사용자");

	private final String name;
}
