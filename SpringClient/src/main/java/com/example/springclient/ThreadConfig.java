package com.example.springclient;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;
import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

public interface ThreadConfig {
    @Bean(APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    default AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(newVirtualThreadPerTaskExecutor());
    }

    @Bean
    default TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(newVirtualThreadPerTaskExecutor());
    }
}


