package dayou.lifemate.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dayou.lifemate.global.interceptor.LocalQueryCountInterceptor;
import dayou.lifemate.global.interceptor.QueryCountInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final QueryCountInterceptor countInterceptor;
	private final LocalQueryCountInterceptor localQueryCountInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(countInterceptor);
		registry.addInterceptor(localQueryCountInterceptor);
	}
}
