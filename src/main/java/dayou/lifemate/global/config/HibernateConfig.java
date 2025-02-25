package dayou.lifemate.global.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dayou.lifemate.global.QueryCountInspector;
import lombok.RequiredArgsConstructor;

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
