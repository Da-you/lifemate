package dayou.lifemate.domain.member.entity;

import dayou.lifemate.domain.member.enums.Role;
import dayou.lifemate.global.BaseTimeEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
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
public class Mate extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 멘토 (Member)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mentor_id", nullable = false,
		foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member mentor;

	// 멘티 (Member)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mentee_id", nullable = false,
		foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member mentee;

	@Builder
	public Mate(Member mentor, Member mentee) {
		if (mentor.getRole() != Role.ROLE_MENTOR) {
			throw new IllegalArgumentException("멘토로 등록된 사용자가 아닙니다.");
		}
		this.mentor = mentor;
		this.mentee = mentee;
	}
}
