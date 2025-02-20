package dayou.lifemate.domain.todo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.member.repository.MemberRepository;
import dayou.lifemate.domain.todo.dto.TodoResponseDto;
import dayou.lifemate.domain.todo.entity.Todo;
import dayou.lifemate.domain.todo.entity.TodoTask;
import dayou.lifemate.domain.todo.repository.TodoRepository;
import dayou.lifemate.domain.todo.repository.TodoTaskRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

	private final MemberRepository memberRepo;
	private final TodoRepository todoRepo;
	private final TodoTaskRepository taskRepo;

	@Transactional
	public void createTodo(String email, LocalDate date) {
		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
		);

		todoRepo.save(Todo.builder().member(member).date(date).build());
	}

	@Transactional
	public void createTask(String email, LocalDate date, String category, String task) {
		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
		);
		Todo todo = todoRepo.findByMemberAndDate(member, date);
		if (todo == null) {
			throw new IllegalArgumentException("일정을 먼저 생성해 주세요");
		}

		taskRepo.save(TodoTask.builder()
			.todo(todo)
			.category(category)
			.task(task)
			.build()
		);
	}

	@Transactional(readOnly = true)
	public List<TodoResponseDto> getCalender(String email) {
		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
		);
		List<TodoResponseDto> res = new ArrayList<>();
		List<Todo> todos = todoRepo.findAllByMember(member);
		for (Todo todo : todos) {
			res.add(TodoResponseDto.builder()
				.todoId(todo.getId())
				.date(todo.getDate())
				.taskCount(todo.getTasks().size())
				.build());
		}
		return res;
	}

	@Transactional(readOnly = true)
	public List<TodoTask> getTasks(String email, Long todoId) {
		Member member = memberRepo.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
		);
		Todo todo = todoRepo.findById(todoId).orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다."));

		List<TodoTask> tasks = taskRepo.findAllByTodo(todo);
		return tasks;

	}
}
