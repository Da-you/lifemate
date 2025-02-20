package dayou.lifemate.domain.todo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dayou.lifemate.domain.todo.dto.TodoResponseDto;
import dayou.lifemate.domain.todo.entity.TodoTask;
import dayou.lifemate.domain.todo.service.TodoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/todos/v1")
@RequiredArgsConstructor
public class TodoApiController {

	private final TodoService todoService;

	@PostMapping
	public String createTodo(String email, @RequestBody LocalDate date) {
		todoService.createTodo(email, date);
		return "일정 생성";
	}

	@PostMapping("/task")
	public String createTask(String email, @RequestBody LocalDate date, String category, String task) {
		todoService.createTask(email, date, category, task);
		return "투두 생성";
	}

	@GetMapping
	public List<TodoResponseDto> getCalender(String email) {
		return todoService.getCalender(email);
	}

	@GetMapping("/{id}")
	public List<TodoTask> geTasks(String email, @PathVariable Long id) {
		return todoService.getTasks(email, id);
	}

}
