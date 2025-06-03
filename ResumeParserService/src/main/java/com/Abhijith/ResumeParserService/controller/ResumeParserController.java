package com.Abhijith.ResumeParserService.controller;

import com.Abhijith.ResumeParserService.feign.ApplicationClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resume-parser")
public class ResumeParserController {

	@Autowired
	private ApplicationClient applicationClient;

	@GetMapping("/applications/{id}/parse-resume")
	public ResponseEntity<Map<String, String>> parseResume(@PathVariable String id) {
	
		ResponseEntity<Map<String, String>> response = applicationClient.getResumeUrl(id);
		
		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			String url = response.getBody().get("resumeUrl");
			
			if (url == null || url.isBlank()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						       .body(Map.of("error", "Resume URL is missing for ID: " + id));
			}
			
			try (InputStream input = new URL(url).openStream();
			     PDDocument document = PDDocument.load(input)) {
				
				PDFTextStripper stripper = new PDFTextStripper();
				String text = stripper.getText(document);
				
				Map<String, String> result = new HashMap<>();
				result.put("parsedText", text);
				return ResponseEntity.ok(result);
				
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(Map.of("error", "Failed to parse resume: " + e.getMessage()));
			}
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				       .body(Map.of("error", "Resume URL not found for ID: " + id));
	}


}
