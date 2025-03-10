package dayou.lifemate.domain.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.events.Event;

import dayou.lifemate.domain.lecture.entity.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Event.ID> {
}
