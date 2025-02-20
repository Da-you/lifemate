package dayou.lifemate.domain.todo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.domain.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	Todo findByMemberAndDate(Member member, LocalDate date);

	List<Todo> findAllByMember(Member member);
}
