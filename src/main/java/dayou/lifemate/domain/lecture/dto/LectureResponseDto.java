package dayou.lifemate.domain.lecture.dto;

import java.time.LocalDateTime;

import dayou.lifemate.domain.lecture.entity.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureResponseDto {
	private Long lectureId;
	private String title;
	private String description;
	private LocalDateTime eventDate;
	private int maxParticipants;
	private int currentParticipants;

	@Builder
	public LectureResponseDto(Lecture lecture) {
		this.lectureId = lecture.getId();
		this.title = lecture.getTitle();
		this.description = lecture.getDescription();
		this.eventDate = lecture.getEventDate();
		this.maxParticipants = lecture.getMaxParticipants();
		this.currentParticipants = lecture.getCurrentParticipants();
	}
}
