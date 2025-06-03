package com.Abhijith.ApplicationService.controller;

import com.Abhijith.ApplicationService.dto.ApplicationDto;
import com.Abhijith.ApplicationService.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationDto> createApplication(
            @RequestParam("jobId") String jobId,
            @RequestParam("studentName") String studentName,
            @RequestParam("email") String email,
            @RequestParam("resumeFile") MultipartFile resumeFile) {
        
        try {
            ApplicationDto createdApp = applicationService.createApplication(
                    jobId, studentName, email, resumeFile);
            return new ResponseEntity<>(createdApp, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDto>> getAllApplications() {
        List<ApplicationDto> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable String id) {
        ApplicationDto applicationDto = applicationService.getApplicationById(id);
        if (applicationDto != null) {
            return ResponseEntity.ok(applicationDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/resume-url")
    public ResponseEntity<Map<String, String>> getResumeUrl(@PathVariable String id) {
        String resumeUrl = applicationService.getResumeUrlById(id);
        if (resumeUrl != null) {
            Map<String, String> response = new HashMap<>();
            response.put("resumeUrl", resumeUrl);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
    
    
}
