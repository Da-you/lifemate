package dayou.lifemate.domain.member.dto.response;

import dayou.lifemate.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
	private Long id;
	private String email;
	private String nickname;
	private String role;

	@Builder
	public MemberResponseDto(Member member) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.role = member.getRole().getName();
	}
}


