 package com.bridgelabz.fundoonotes.user.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

	private final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

	
    private RestHighLevelClient restHighLevelClient;

    @Bean
	public RestHighLevelClient buildClient() {
		try {
			 restHighLevelClient = new RestHighLevelClient(
	                    RestClient.builder(
	                            new HttpHost("localhost", 9200, "http")));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return restHighLevelClient;
	}


}
