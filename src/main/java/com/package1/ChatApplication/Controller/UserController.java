package com.package1.ChatApplication.Controller;	

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.package1.ChatApplication.Entity.User;
import com.package1.ChatApplication.MessageRepository.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class UserController {

	@Autowired 
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@PostMapping("/signup")
	public String signup(@RequestBody User user) {
		
		User foundUser = userRepo.findByEmail(user.getEmail());
		
		if(foundUser != null){
			return "User ALeready Exist with this mail !!"; 
		}
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
		return "Registered Successfull..";
	}
	
	@PostMapping("/login")
	public String login(@RequestBody User user) {
		
		User foundUser = userRepo.findByEmail(user.getEmail());
		
		if(foundUser != null) {
			if(encoder.matches(user.getPassword(), foundUser.getPassword())) {
				return foundUser.getDisplayName();
		    }
		    else {
				return "Invalid Password";
		    }
		}
		else {
			return "User Not Found";
		}
	}
	
}
