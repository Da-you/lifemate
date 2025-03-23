package dayou.lifemate.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(15);      // 기본 실행 대기 스레드 수
		executor.setMaxPoolSize(30);      // 최대 스레드 수
		executor.setQueueCapacity(50);    // 최대 큐 크기
		executor.setThreadNamePrefix("AsyncThread-");
		executor.initialize();
		return executor;
	}

	@Bean(name = "interestExecutor")
	public Executor interestExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);      // 기본 실행 대기 스레드 수
		executor.setMaxPoolSize(10);      // 최대 스레드 수
		executor.setQueueCapacity(25);    // 최대 큐 크기
		executor.setThreadNamePrefix("AsyncThread-");
		executor.initialize();
		return executor;
	}
}
