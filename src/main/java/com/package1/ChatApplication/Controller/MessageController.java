package com.package1.ChatApplication.Controller;

import java.util.ArrayList	;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.package1.ChatApplication.MessageRepository.MessageRepository;
import com.package1.ChatApplication.EncryptionAndDecryption.AESEncryption;
import com.package1.ChatApplication.Entity.Messages;

@RestController
public class MessageController {

	@Autowired
	private MessageRepository msgrepo;
	
	@Autowired
	private AESEncryption aesencrypt;
	
	@GetMapping("/messages/{username}")
	public List<Messages> getMessages(@PathVariable String username){
		List<Messages> allMessages = msgrepo.findAll();
		List<Messages> result= new ArrayList<>();
		
		for(Messages m: allMessages) {
			if(!m.isPrivate() || m.getSender().equals(username) || m.getRecipient().equals(username)) {
				 Messages decryptedMsg = new Messages();
		            decryptedMsg.setId(m.getId()); 
		            decryptedMsg.setSender(m.getSender());
		            decryptedMsg.setRecipient(m.getRecipient());
		            decryptedMsg.setTime(m.getTime());
		            decryptedMsg.setPrivate(m.isPrivate());
		            decryptedMsg.setContent(aesencrypt.decrypt(m.getContent())); 
		            result.add(decryptedMsg);
			}
		}
		return result;
	}
}
