package com.Abhijith.ResumeParserService.controller;

import com.Abhijith.ResumeParserService.feign.ApplicationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resume-parser")
public class ApplicationController {

	@Autowired
    private ApplicationClient applicationClient;
	
    @GetMapping("/applications")
    public List<Map<String, Object>> getAllApplications() {
        return applicationClient.getAllApplications();
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<Map<String, Object>> getApplicationById(@PathVariable String id) {
        return applicationClient.getApplicationById(id);
    }

    @GetMapping("/applications/{id}/resume-url")
    public ResponseEntity<Map<String, String>> getResumeUrl(@PathVariable String id) {
        return applicationClient.getResumeUrl(id);
    }
    
}
