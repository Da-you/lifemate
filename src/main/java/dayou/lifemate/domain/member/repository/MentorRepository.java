package dayou.lifemate.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.Mentor;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
	boolean existsByMember(Member member);

	Mentor findByMember(Member member);

	Page<Mentor> findAll(Pageable pageable);
}
