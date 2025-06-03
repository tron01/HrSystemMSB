package com.Abhijith.ResumeParserService.controller;

import com.Abhijith.ResumeParserService.dto.ResumeParseRequest;
import com.Abhijith.ResumeParserService.service.ResumeParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/resume-parser")
@RequiredArgsConstructor
public class ResumeParserController {
	
	private final ResumeParserService resumeParserService;
	
	@PostMapping("/parse")
	public ResponseEntity<String> parseResume(@RequestBody ResumeParseRequest request) {
		try {
			resumeParserService.parseAndStore(request);
			return ResponseEntity.ok("Resume parsed and stored successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body("Failed to parse resume: " + e.getMessage());
		}
	}
	
}
