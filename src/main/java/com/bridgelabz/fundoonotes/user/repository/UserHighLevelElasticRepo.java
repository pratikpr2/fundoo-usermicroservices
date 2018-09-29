package com.bridgelabz.fundoonotes.user.repository;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class UserHighLevelElasticRepo implements HighLevelElasticRepo{

	@Value("${index}")
	String INDEX;
	
	@Value("${type}")
	String TYPE;

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void save(User user) {
		@SuppressWarnings("rawtypes")
		Map dataMap = objectMapper.convertValue(user, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, user.getUserId()).source(dataMap);

		try {
			@SuppressWarnings("unused")
			IndexResponse response = restHighLevelClient.index(indexRequest);

		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}

	}

	@Override
	public Optional<User> findByUserEmail(String userEmail) {

		SearchRequest searchRequest = new SearchRequest(INDEX);
		searchRequest.types(TYPE);

		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchPhrasePrefixQuery("userEmail", userEmail));

		searchRequest.source(sourceBuilder);

		Optional<User> myUser = null;
		SearchResponse searchResponse = null;
		String user;

		try {
			searchResponse = restHighLevelClient.search(searchRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		SearchHit[] hits = searchResponse.getHits().getHits();

		try {
			user = hits[0].getSourceAsString();
		} catch (Exception e) {
			return myUser;
		}

		try {
			myUser = Optional.of(objectMapper.readValue(user, User.class));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return myUser;
	}

	@Override
	public Optional<User> findById(String userId){
		GetRequest getRequest = new GetRequest(INDEX,TYPE, userId);

		GetResponse getResponse = null;

		Optional<User> optionalUser = null;

		try {
			getResponse = restHighLevelClient.get(getRequest);

			String userData = getResponse.getSourceAsString();

			optionalUser = Optional.of(objectMapper.readValue(userData, User.class));
		} catch (IOException exception) {
			return optionalUser;
		}

		return optionalUser;
	}

}
