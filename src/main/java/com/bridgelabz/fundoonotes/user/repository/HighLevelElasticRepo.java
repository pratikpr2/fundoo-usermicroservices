package com.bridgelabz.fundoonotes.user.repository;

import java.util.Optional;

import com.bridgelabz.fundoonotes.user.model.User;

public interface HighLevelElasticRepo {

	public void save(User user);
	public Optional<User> findByUserEmail(String userEmail);
	public Optional<User> findById(String userId);
	
}
