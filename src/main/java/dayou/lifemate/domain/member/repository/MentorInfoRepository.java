package dayou.lifemate.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.domain.member.enums.Field;

public interface MentorInfoRepository extends JpaRepository<MentorInfo, Long> {
	boolean existsByMember(Member member);

	MentorInfo findByMember(Member member);

	Page<MentorInfo> findAll(Pageable pageable);

	/* 같은 분야의 멘토 중 totalRating이 더 높은 멘토 수를 반환
	 * field = IT, totalRating = 95인 멘토가 있을 때,
	 *같은 IT 분야에서 totalRating > 95인 멘토가 4명이 있다면,
	 * 이 멘토의 랭킹은 5위입니다 → 4 (더 높은 사람) + 1 (본인)
	 */
	Integer countByFieldAndTotalRatingGreaterThan(Field field, long totalRating);
}
