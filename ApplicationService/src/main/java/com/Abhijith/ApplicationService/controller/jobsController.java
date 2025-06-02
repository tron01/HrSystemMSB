package com.Abhijith.ApplicationService.controller;

import com.Abhijith.ApplicationService.feign.JobClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
public class jobsController {

    @Autowired
    private JobClient jobClient;

    @GetMapping("/jobs")
    public List<Map<String, Object>> getAllJobs() {
        return jobClient.getAllJobs();
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Map<String, Object>> getJobById(@PathVariable String id) {
        Map<String, Object> job = jobClient.getJobById(id);
        if (job != null) {
            return ResponseEntity.ok(job);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
