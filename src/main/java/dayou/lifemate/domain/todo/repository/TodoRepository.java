package dayou.lifemate.domain.todo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.todo.entity.Todo;
import dayou.lifemate.domain.todo.enums.Category;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	Todo findByMemberAndDate(Member member, LocalDate date);

	List<Todo> findAllByMember(Member member);

	Page<Todo> findAllByCategoryAndDate(Category category, LocalDate today, Pageable pageable);

	@Query("select t from Todo t "
		+ "where t.date = :date "
		+ "and t.category = :category "
		+ "order by t.createdAt DESC")
	@EntityGraph(attributePaths = {"tasks", "member"})
	Page<Todo> findTodosByComplexCondition(
		@Param("date") LocalDate date,
		@Param("category") Category category,
		Pageable pageable
	);
}
