package dayou.lifemate.domain.todo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dayou.lifemate.domain.member.service.MemberLoginService;
import dayou.lifemate.domain.todo.dto.TodoRequestDto;
import dayou.lifemate.domain.todo.dto.TodoResponseDto;
import dayou.lifemate.domain.todo.dto.TodoResponseDto.TodoCreateResponseDto;
import dayou.lifemate.domain.todo.dto.TodoResponseDto.TodoResponseWithTaskCountDto;
import dayou.lifemate.domain.todo.enums.Category;
import dayou.lifemate.domain.todo.entity.TodoTask;
import dayou.lifemate.domain.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/todos/v1")
@RequiredArgsConstructor
public class TodoApiController {

	private final TodoService todoService;
	private final MemberLoginService loginService;

	@PostMapping
	public ResponseEntity<TodoCreateResponseDto> createTodo(@AuthenticationPrincipal User user,
		@RequestBody TodoRequestDto req) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(todoService.createTodo(email, req));
	}

	@GetMapping("/calender")
	public ResponseEntity<List<TodoResponseWithTaskCountDto>> getCalender(@AuthenticationPrincipal User user) {
		String email = loginService.getCurrentMember(user.getUsername());
		return ResponseEntity.ok(todoService.getCalender(email));
	}

	@GetMapping("/{id}")
	public List<TodoTask> geTasks(@AuthenticationPrincipal User user, @PathVariable Long id) {
		String email = loginService.getCurrentMember(user.getUsername());
		return todoService.getTasks(email, id);
	}

	@GetMapping
	public ResponseEntity<Page<TodoResponseDto>> getTodos(
		@RequestParam(required = false) LocalDate date,
		@RequestParam(required = false) Category category,
		Pageable pageable) {
		return ResponseEntity.ok(todoService.getTodoList(date,category, pageable));
	}

}
