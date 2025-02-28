package dayou.lifemate.domain.review.entity;

import dayou.lifemate.domain.member.entity.MentorInfo;
import dayou.lifemate.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "info_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MentorInfo info;

	@Column(nullable = false)
	private Long menteeId;

	@Column(nullable = false)
	private Long mentorId;

	private String content;

	private int rating;

	@Builder
	public Review(MentorInfo info, Long menteeId, String content, int rating) {
		this.info = info;
		this.menteeId = menteeId;
		this.mentorId = info.getMember().getId();
		this.content = content;
		this.rating = rating;
	}

	public void updateReview(String content, int rating) {
		this.content = content;
		this.rating = rating;
	}
}
