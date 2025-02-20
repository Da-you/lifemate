package dayou.lifemate.domain.todo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponseDto {
	private Long todoId;
	private LocalDate date;
	private int taskCount;

}
