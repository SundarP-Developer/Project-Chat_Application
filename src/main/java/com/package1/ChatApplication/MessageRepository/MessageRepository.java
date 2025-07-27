package com.package1.ChatApplication.MessageRepository;

import org.springframework.data.jpa.repository.JpaRepository;	
import org.springframework.stereotype.Repository;

import com.package1.ChatApplication.Entity.Messages;


@Repository
public interface MessageRepository extends JpaRepository<Messages,Long>{

}
