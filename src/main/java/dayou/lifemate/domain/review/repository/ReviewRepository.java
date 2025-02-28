package dayou.lifemate.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dayou.lifemate.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Review findByIdAndMenteeId(Long id, Long menteeId);

}
