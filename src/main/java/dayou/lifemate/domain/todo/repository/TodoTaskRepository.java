package dayou.lifemate.domain.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.todo.entity.Todo;
import dayou.lifemate.domain.todo.entity.TodoTask;

public interface TodoTaskRepository extends JpaRepository<TodoTask, Long> {
	List<TodoTask> findAllByTodo(Todo todo);

}
