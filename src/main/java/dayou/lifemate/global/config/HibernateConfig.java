package dayou.lifemate.global.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dayou.lifemate.global.QueryCountInspector;
import lombok.RequiredArgsConstructor;
// 직접 작성한 쿼리 카운터를 빈으로 등록
@Configuration
@RequiredArgsConstructor
public class HibernateConfig {

	private final QueryCountInspector counter;

	@Bean
	public HibernatePropertiesCustomizer configureStatementInspector() {
		return hibernateProperties ->
			hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, counter);
	}

}
