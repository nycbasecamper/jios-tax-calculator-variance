package gov.irs.jios.config;


import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ETCSAsyncConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		   executor.setCorePoolSize(4);
		   executor.setMaxPoolSize(4);
		   executor.setQueueCapacity(50);
		   executor.setThreadNamePrefix("AsynchThread::");
		   executor.initialize();
		return executor;
	
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> {
			System.out.println("Exception with message :" + ex.getMessage());
			System.out.println("Method :" + method.toString());
			System.out.println("Number of parameters :" + params.length);
		};

	}
}