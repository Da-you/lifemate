package dayou.lifemate.domain.lecture.entity;

import java.time.LocalDateTime;

import dayou.lifemate.domain.member.entity.Member;
import dayou.lifemate.global.BaseTimeEntity;
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
public class Lecture extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mentor_id")
	private Member mentor;

	private String title;
	private String description;
	private LocalDateTime eventDate;
	private int maxParticipants;
	private int currentParticipants;

	@Builder
	public Lecture(Member mentor, String title, String description, LocalDateTime eventDate, int maxParticipants) {
		this.mentor = mentor;
		this.title = title;
		this.description = description;
		this.eventDate = eventDate;
		this.maxParticipants = maxParticipants;
		this.currentParticipants = 0;
	}

	public void increaseParticipants() {
		if (this.currentParticipants >= this.maxParticipants) {
			throw new RuntimeException("최대 참가 인원을 초과했습니다.");
		}
		this.currentParticipants++;
	}
}
