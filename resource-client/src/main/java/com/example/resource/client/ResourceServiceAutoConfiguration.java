package com.example.resource.client;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@AutoConfiguration
public class ResourceServiceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ResourceService resourceService(RestClient restClient) {
        return HttpServiceProxyFactory.builderFor(
                                              RestClientAdapter.create(
                                                      restClient
                                              )
                                      )
                                      .build()
                                      .createClient(ResourceService.class);
    }
}