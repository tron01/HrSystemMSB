package com.Abhijith.ResumeParserService.controller;

import com.Abhijith.ResumeParserService.model.ParsedResume;
import com.Abhijith.ResumeParserService.service.ResumeParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/parsed-data")
@RequiredArgsConstructor
public class ParsedDataController {

	private final ResumeParserService resumeParserService;
	
	@GetMapping
	public ResponseEntity<List<ParsedResume>> getAllParsedResumes() {
		List<ParsedResume> resumes = resumeParserService.getAllParsedResumes();
		return ResponseEntity.ok(resumes);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ParsedResume> getParsedResumeById(@PathVariable String id) {
		ParsedResume resume = resumeParserService.getParsedResumeById(id);
		if (resume != null) {
			return ResponseEntity.ok(resume);
		}
		return ResponseEntity.notFound().build();
	}

}