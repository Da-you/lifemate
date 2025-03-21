package dayou.lifemate.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.MenteeInterest;
import dayou.lifemate.domain.member.enums.Field;

public interface MenteeInterestRepository extends JpaRepository<MenteeInterest, Long> {
	boolean existsByMenteeAndField(Member mentee, Field field);

	MenteeInterest findByMenteeAndField(Member mentee, Field field);
}
