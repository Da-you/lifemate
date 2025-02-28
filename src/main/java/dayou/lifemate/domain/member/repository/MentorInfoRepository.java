package dayou.lifemate.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.MentorInfo;

public interface MentorInfoRepository extends JpaRepository<MentorInfo, Long> {
	boolean existsByMember(Member member);

	MentorInfo findByMember(Member member);

	Page<MentorInfo> findAll(Pageable pageable);
}
