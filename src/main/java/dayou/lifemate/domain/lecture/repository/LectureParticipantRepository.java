package dayou.lifemate.domain.lecture.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dayou.lifemate.domain.lecture.entity.LectureParticipant;

public interface LectureParticipantRepository extends JpaRepository<LectureParticipant, Long> {

	@Query("SELECT COUNT(lp) FROM LectureParticipant lp WHERE lp.lecture.id = :lectureId")
	long countByLectureId(@Param("lectureId") Long lectureId);

	List<LectureParticipant> findByExternalIdIsNullAndCreatedAtBefore(LocalDateTime dateTime);
}
