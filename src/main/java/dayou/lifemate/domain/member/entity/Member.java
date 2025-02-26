package dayou.lifemate.domain.member.entity;

import dayou.lifemate.domain.member.enums.Role;
import dayou.lifemate.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false, unique = true)
	private String nickname;
	@Enumerated(EnumType.STRING)
	private Role role = Role.ROLE_USER;

	@Builder
	public Member(String email, String password, String nickname, Role role) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}

	public void registerMentor() {
		this.role = Role.ROLE_MENTOR;
	}
}
