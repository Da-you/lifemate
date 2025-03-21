package dayou.lifemate.domain.lecture.external;

import static com.slack.api.model.block.composition.BlockCompositions.*;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.slack.api.Slack;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.webhook.Payload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessageApi {

	@Value("${slack.webhook.url}")
	private String url;
	private final Slack slack = Slack.getInstance();
	private StringBuilder sb = new StringBuilder();

	public void sendLectureJoinMessage(String title, String email, String externalId) {
		// Slack에 전달할 Payload 구성
		Payload payload = Payload.builder()
			.blocks(generateLayBlock(title, email, externalId))
			.build();
		try {
			slack.send(url, payload);                     // Slack 메시지 전송
			// WebhookResponse(code=200, message=OK, body=ok)
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<LayoutBlock> generateLayBlock(String title, String email, String externalId) {
		return Blocks.asBlocks(
			getSection(generateMessage(title, email, externalId))
		);
	}

	private String generateMessage(String title, String email, String externalId) {
		sb.setLength(0);
		sb.append("[예매 정보]");
		sb.append("강연명 :" + title);
		sb.append("예약자 이메일 :" + email);
		sb.append("결제 ID :" + externalId);
		return sb.toString();
	}

	private LayoutBlock getSection(String message) {
		return Blocks.section(s -> s.text(plainText(message)));
	}
}
