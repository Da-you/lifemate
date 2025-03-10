package dayou.lifemate.domain.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.lecture.entity.LectureParticipant;

public interface LectureParticipantRepository extends JpaRepository<LectureParticipant, Long> {
}
