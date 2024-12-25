package com.genaichat.message.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ServerController {
	
	@GetMapping("/check")
	public String getCheck() {
		return "check validate";
	}
}
