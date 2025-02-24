package dayou.lifemate.domain.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.todo.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
