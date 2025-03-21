package dayou.lifemate.domain.member.entity;

import dayou.lifemate.domain.member.enums.Field;
import jakarta.persistence.Column;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenteeInterest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mentee_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member mentee;
	@Column(name = "interest_field")
	@Enumerated(value = EnumType.STRING)
	private Field field;

	private long viewCount;

	@Builder
	public MenteeInterest(Member mentee, Field field) {
		this.mentee = mentee;
		this.field = field;
		this.viewCount = 1;
	}

	public void updateViewCount() {
		this.viewCount += 1;
	}
}
