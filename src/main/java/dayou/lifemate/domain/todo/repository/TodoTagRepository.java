package dayou.lifemate.domain.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.todo.entity.TodoTag;

public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {
}
