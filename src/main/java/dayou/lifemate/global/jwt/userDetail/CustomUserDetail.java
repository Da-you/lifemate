package dayou.lifemate.global.jwt.userDetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.enums.Role;

public class CustomUserDetail implements UserDetails {

	private final Member member;
	private final String email;

	public CustomUserDetail(Member member, String email) {
		this.member = member;
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Role role = member.getRole();
		String auth = role.getName();

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(auth);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(simpleGrantedAuthority);

		return authorities;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // 계정이 만료되지 않음
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // 계정이 잠겨있지 않음
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 비밀번호가 만료되지 않음
	}

	@Override
	public boolean isEnabled() {
		return true; // 계정이 활성화됨
	}
}
