package com.package1.ChatApplication.MessageRepository;	

import org.springframework.data.jpa.repository.JpaRepository;	
import org.springframework.stereotype.Repository;
import com.package1.ChatApplication.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	User findByEmail(String username);
	
}
