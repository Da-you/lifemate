package dayou.lifemate.domain.todo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import dayou.lifemate.domain.todo.entity.Category;
import dayou.lifemate.domain.todo.entity.Todo;
import dayou.lifemate.domain.todo.entity.TodoTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponseDto {
	private Long todoId;
	private String nickname;
	private LocalDate date;
	private Category category;
	private List<String> task;
	private List<String> tags;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Builder
	public TodoResponseDto(Todo todo) {
		this.todoId = todo.getId();
		this.nickname = todo.getMember().getNickname();
		this.date = todo.getDate();
		this.category = todo.getCategory();
		this.task = todo.getTasks().stream()
			.map(TodoTask::getTask).toList();
		this.tags = todo.getTags().stream()
			.map(tag -> tag.getTag().getName()).toList();
		this.createdAt = todo.getCreatedAt();
		this.updatedAt = todo.getUpdatedAt();
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TodoResponseWithTaskCountDto {
		private Long todoId;
		private LocalDate date;
		private int taskCount;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TodoCreateResponseDto {
		private Long todoId;
		private LocalDate date;
		private String task;
	}
}
