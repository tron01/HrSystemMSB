package com.Abhijith.ResumeParserService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ApplicationService")
public interface ApplicationClient {
	@GetMapping("/applications")
	List<Map<String, Object>> getAllApplications();
	
	@GetMapping("/applications/{id}")
	ResponseEntity<Map<String, Object>> getApplicationById(@PathVariable String id);

	@GetMapping("/applications/{id}/resume-url")
	ResponseEntity<Map<String, String>> getResumeUrl(@PathVariable String id);
}
