package dayou.lifemate.domain.lecture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dayou.lifemate.domain.lecture.entity.Lecture;
import jakarta.persistence.LockModeType;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select l from Lecture l where l.id = :id")
	Optional<Lecture> findByIdUsePessimistic(Long id);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("select l from Lecture l where l.id = :id")
	Optional<Lecture> findByIdUseOpimistic(Long id);

	// 네임드 락 락 설정
	@Query(value = "SELECT GET_LOCK(:lockName, :timeoutSeconds)", nativeQuery = true)
	Integer getLock(@Param("lockName") String lockName, @Param("timeoutSeconds") int timeoutSeconds);

	@Query(value = "SELECT RELEASE_LOCK(:lockName)", nativeQuery = true)
	Integer releaseLock(@Param("lockName") String lockName);
}

