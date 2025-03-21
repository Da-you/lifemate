package dayou.lifemate.domain.lecture.external;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dayou.lifemate.domain.lecture.dto.ExternalPayResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExternalPayApi {

	private static final Random random = new Random();

	public ExternalPayResponseDto registerParticipant(Long id, String email) {
		try {
			Thread.sleep(random.nextInt(500, 1500));
			return ExternalPayResponseDto.builder()
				.success(true)
				.externalId(UUID.randomUUID().toString())
				.lectureId(id)
				.email(email)
				.errorMessage(null)
				.build();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);

		}
	}

	public ExternalPayResponseDto getParticipantInfo(Long id, String email) {
		try {
			Thread.sleep(100);
			return ExternalPayResponseDto.builder()
				.success(true)
				.externalId(UUID.randomUUID().toString())
				.lectureId(id)
				.email(email)
				.errorMessage(null)
				.build();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
