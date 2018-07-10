#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service.boot;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <br>created at 18-3-29
 *
 * @author liuxh
 * @version 1.0.0
 */
@Configuration
@PropertySource(value = "classpath:mybatisDruid.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:redis.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:manager-config.properties", ignoreResourceNotFound = true)
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableCaching
@ComponentScan({
        "${package}",
        "cn.howardliu.gear.springEx.controller"
})
public class ArchetypeServiceConfiguration {
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setKeepAliveSeconds(200);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
