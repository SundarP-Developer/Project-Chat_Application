package com.package1.ChatApplication.ChatHandler;

import java.io.IOException;			
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.package1.ChatApplication.MessageRepository.MessageRepository;
import com.package1.ChatApplication.EncryptionAndDecryption.AESEncryption;
import com.package1.ChatApplication.Entity.Messages;

@Component
public class ChatHandler extends TextWebSocketHandler {
	
	public ChatHandler() {}
	
	@Autowired
	private MessageRepository msgrepo;
	
	@Autowired
	private AESEncryption aesencrypt;

	private Map<WebSocketSession,String> userNames=new HashMap();
	private List<WebSocketSession> sessions=new ArrayList<>();
	
	public void afterConnectionEstablished(WebSocketSession session) throws IOException {
		sessions.add(session);
	}
	
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
		String msg=message.getPayload();
		
		if (!userNames.containsKey(session)) {
		    userNames.put(session, msg);
		    sendToAll("✅ "+msg + " joined the chat 🎉");
		    return;
		}
		
		String sender=userNames.get(session);
		
		Messages m =new Messages();
		m.setSender(sender);
		m.setTime(LocalDateTime.now());
		

		String name = userNames.get(session);
		
		if(msg.startsWith("@")) {
			int space=msg.indexOf(" ");
			
			if(space!=-1) {
				String targetName=msg.substring(1, space);
				String privateMsg=msg.substring(space+1);
				String fullMsg="[Private] "+ name + " : " +privateMsg;
				
				m.setRecipient(targetName);
				m.setContent(aesencrypt.encrypt(privateMsg));
				m.setPrivate(true);
				msgrepo.save(m);
				
				for(Map.Entry<WebSocketSession, String> entry:userNames.entrySet()) {
					if(entry.getValue().equals(targetName)) {
						entry.getKey().sendMessage(new TextMessage(fullMsg));
						session.sendMessage(new TextMessage(fullMsg));
						return;
					}
				}
				session.sendMessage(new TextMessage("User '" + targetName + "' not found."));
		        return;
			}
		}
		m.setContent(aesencrypt.encrypt(msg));
		m.setPrivate(false);
		msgrepo.save(m);
		
		sendToAll(name +": "+ msg);
	}	 
	
	public void afterConnectionClosed(WebSocketSession session,CloseStatus status) throws IOException {
		String name=userNames.getOrDefault(session, "Unknown");
		sessions.remove(session);
		userNames.remove(session);
		sendToAll(name+" left the chat...");
	}
	
	public void sendToAll(String message) throws IOException {
		for (WebSocketSession session : sessions) {
			try {
	             session.sendMessage(new TextMessage(message));
	             String users = "USERS:" + String.join(",", userNames.values());
	             session.sendMessage(new TextMessage(users));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}