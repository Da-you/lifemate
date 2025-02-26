package dayou.lifemate.domain.member.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.member.entity.Mate;
import dayou.lifemate.domain.member.entity.Member;

public interface MateRepository extends JpaRepository<Mate, Long> {
	boolean existsByMenteeAndMentor(Member mentee, Member mentor);

	Mate findByMenteeAndMentor(Member mentee, Member mentor);

	Mate findByMenteeAndId(Member mentee, Long id);

	Page<Mate> findAllByMentee(Member mentee, Pageable pageable);

	List<Mate> findAllByMentee(Member mentee);
}
