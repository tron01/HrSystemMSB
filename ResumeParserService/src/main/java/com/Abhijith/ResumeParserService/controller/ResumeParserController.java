package com.Abhijith.ResumeParserService.controller;

import com.Abhijith.ResumeParserService.dto.ResumeParseRequest;
import com.Abhijith.ResumeParserService.service.ResumeParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/resume-parser")
@RequiredArgsConstructor
@Slf4j
public class ResumeParserController {
	
	private final ResumeParserService resumeParserService;
	
	@PostMapping("/parse")
	public ResponseEntity<String> parseResume(@RequestBody ResumeParseRequest request) {
		try {
			resumeParserService.parseAndStore(request);
			return ResponseEntity.ok("Resume parsed and stored successfully.");
		} catch (Exception e) {
			log.error("Failed to parse resume", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body("Failed to parse resume: " + e.getMessage());
		}
	}
	
}
