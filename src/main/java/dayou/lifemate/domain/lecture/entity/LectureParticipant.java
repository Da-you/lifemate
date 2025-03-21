package dayou.lifemate.domain.lecture.entity;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.global.BaseTimeEntity;
import jakarta.persistence.Column;
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
public class LectureParticipant extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_id",
		foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Lecture lecture;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id",
		foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;
	// 외부 api를 통한 결제 ID
	@Column(name = "external_id")
	private String externalId;

	@Builder
	public LectureParticipant(Lecture lecture, Member member) {
		this.lecture = lecture;
		this.member = member;
	}

	public void updatedExternalId(String externalId) {
		this.externalId = externalId;
	}
}
