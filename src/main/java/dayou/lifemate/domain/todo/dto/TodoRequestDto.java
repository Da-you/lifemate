package dayou.lifemate.domain.todo.dto;

import java.time.LocalDate;

import dayou.lifemate.domain.todo.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequestDto {
	private LocalDate date;
	private Category category;
	private String task;
}
