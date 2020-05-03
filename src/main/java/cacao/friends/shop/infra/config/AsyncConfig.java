package cacao.friends.shop.infra.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
	
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		int processors = Runtime.getRuntime().availableProcessors();
		log.info("processors count {}", processors);
		executor.setCorePoolSize(processors);			// 풀 사이즈
		executor.setQueueCapacity(50);					// 풀의 갯수 후 대기자
		executor.setMaxPoolSize(processors * 2);		// 대기자가 꽉찰 경우 풀이 사이즈 늘림
		executor.setKeepAliveSeconds(60);				// 늘린 풀이 유효 시간
		executor.setThreadNamePrefix("AsynceExcutor");
		executor.initialize();
		return executor;
	}

}
