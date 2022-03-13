package com.baojikouyu.teach.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class esConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder(
                new HttpHost("localhost", 9200)).build();
    }

    // Create the transport with a Jackson mapper
    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {

        return new RestClientTransport(
                restClient, new JacksonJsonpMapper());
    }

    // And create the API client
    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport) {

        return new ElasticsearchClient(elasticsearchTransport);
    }

}
