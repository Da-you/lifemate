package dayou.lifemate.domain.todo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.domain.todo.dto.TodoRequestDto;
import dayou.lifemate.domain.todo.dto.TodoResponseDto;
import dayou.lifemate.domain.todo.dto.TodoResponseDto.TodoCreateResponseDto;
import dayou.lifemate.domain.todo.dto.TodoResponseDto.TodoResponseWithTaskCountDto;
import dayou.lifemate.domain.todo.enums.Category;
import dayou.lifemate.domain.todo.entity.Todo;
import dayou.lifemate.domain.todo.entity.TodoTask;
import dayou.lifemate.domain.todo.repository.TodoRepository;
import dayou.lifemate.domain.todo.repository.TodoTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

	private final MemberRepository memberRepo;
	private final TodoRepository todoRepo;
	private final TodoTaskRepository taskRepo;

	@Transactional
	public TodoCreateResponseDto createTodo(String email, TodoRequestDto req) {
		log.info(email);
		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
		);
		Todo todo = Todo.builder().member(member).date(req.getDate()).category(req.getCategory()).build();
		todoRepo.save(todo);

		TodoTask task = TodoTask.builder().todo(todo).task(req.getTask()).build();
		taskRepo.save(task);
		return TodoCreateResponseDto.builder().todoId(todo.getId()).date(todo.getDate()).task(task.getTask()).build();
	}

	@Transactional(readOnly = true)
	public List<TodoResponseWithTaskCountDto> getCalender(String email) {
		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
		);
		List<TodoResponseWithTaskCountDto> res = new ArrayList<>();
		List<Todo> todos = todoRepo.findAllByMember(member);
		for (Todo todo : todos) {
			res.add(TodoResponseWithTaskCountDto.builder()
				.todoId(todo.getId())
				.date(todo.getDate())
				.taskCount(todo.getTasks().size())
				.build());
		}
		return res;
	}

	@Transactional(readOnly = true)
	public List<TodoTask> getTasks(String email, Long todoId) {
		Todo todo = todoRepo.findById(todoId).orElseThrow(
			() -> new IllegalArgumentException("일정이 존재하지 않습니다."));

		return taskRepo.findAllByTodo(todo);
	}

	@Transactional(readOnly = true)
	public Page<TodoResponseDto> getTodoList(LocalDate date, Category category, Pageable pageable) {
		Page<Todo> todos = category == null ? todoRepo.findAll(pageable) :
			todoRepo.findTodosByComplexCondition(date, category, pageable);

		return todos.map(TodoResponseDto::new);
	}
}
