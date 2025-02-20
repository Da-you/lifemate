package dayou.lifemate.global.jwt.userDetail;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	private final MemberRepository memberRepo;

	@Override
	public UserDetails loadUserByUsername(String email) {
		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new UsernameNotFoundException("존재하지 않는 유저")
		);
		return new CustomUserDetail(member, member.getEmail());
	}
}
