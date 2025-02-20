package dayou.lifemate.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmailAndPassword(String email, String password);

	boolean existsByEmail(String email);

	Optional<Member> findByEmail(String email);
}
