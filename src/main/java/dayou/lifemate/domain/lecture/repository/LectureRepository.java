package dayou.lifemate.domain.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.lecture.entity.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
