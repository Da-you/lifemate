package dayou.lifemate.domain.todo.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.global.BaseTimeEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;
	@Enumerated(EnumType.STRING)
	private Category category;

	private LocalDate date;

	@OneToMany
	private Set<TodoTask> tasks = new HashSet<>();

	@Builder
	public Todo(Member member, LocalDate date, Category category) {
		this.member = member;
		this.date = date;
		this.category = category;
	}
}
