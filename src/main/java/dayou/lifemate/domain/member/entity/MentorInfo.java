package dayou.lifemate.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import dayou.lifemate.domain.member.enums.Field;
import dayou.lifemate.domain.review.entity.Review;
import dayou.lifemate.global.BaseTimeEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
public class MentorInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@Enumerated(EnumType.STRING)
	private Field field;

	private String job;

	private int career;

	private String description;

	@OneToMany(mappedBy = "info")
	private List<Review> reviews = new ArrayList<>();

	@Builder
	public MentorInfo(Member member, Field field, String job, int career, String description) {
		this.member = member;
		this.field = field;
		this.job = job;
		this.career = career;
		this.description = description;
	}

	public void updateInfo(Field field, String job, int career, String description) {
		this.field = field;
		this.job = job;
		this.career = career;
		this.description = description;
	}
}
