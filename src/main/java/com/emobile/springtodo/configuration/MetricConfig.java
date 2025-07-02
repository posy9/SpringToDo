package com.emobile.springtodo.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfig {

    @Bean
    MeterBinder meterBinder() {
        return meterRegistry ->
            Counter.builder("request.count")
                    .description("Total number of requests")
                    .register(meterRegistry);
    }
}
