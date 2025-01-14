package com.yuseogi.batchserver.config;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

import static io.micrometer.prometheus.PrometheusConfig.DEFAULT;

@Slf4j
@Configuration
public class PrometheusConfig {

    @Value("${prometheus.job.name}")
    private String prometheusJobName;

    @Value("${prometheus.grouping.key}")
    private String prometheusGroupingKey;

    @Value("${prometheus.pushgateway.url}")
    private String prometheusPushGatewayUrl;

    private Map<String, String> groupingKey = new HashMap<>();

    private PushGateway pushGateway;

    private CollectorRegistry collectorRegistry;

    @PostConstruct
    public void init() {
        pushGateway = new PushGateway(prometheusPushGatewayUrl);
        groupingKey.put(prometheusGroupingKey, prometheusJobName);
        PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(DEFAULT);
        collectorRegistry = prometheusMeterRegistry.getPrometheusRegistry();
        Metrics.globalRegistry.add(prometheusMeterRegistry);
    }

    @Scheduled(fixedRateString = "${prometheus.push.rate}")
    public void pushMetrics() {
        try {
            pushGateway.pushAdd(collectorRegistry, prometheusJobName, groupingKey);
        }
        catch (Throwable ex) {
        }
    }

}
