package com.example.Mysql.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Mysql.Entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	
	

}
