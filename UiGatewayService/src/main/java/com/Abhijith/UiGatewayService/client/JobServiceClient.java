package com.Abhijith.UiGatewayService.client;

import com.Abhijith.UiGatewayService.dto.JobDTO;
import com.Abhijith.UiGatewayService.dto.PageableResponse;
import com.Abhijith.UiGatewayService.dto.jobRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "job-posting-service")
public interface JobServiceClient {

    // Public endpoints
    @GetMapping("/api/jobs")
    ResponseEntity<PageableResponse<JobDTO>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );

    @GetMapping("/api/jobs/{id}")
    ResponseEntity<JobDTO> getJobById(@PathVariable("id") String id);

    // HR endpoints
    @PostMapping("/api/jobs")
    ResponseEntity<JobDTO> createJob(@RequestBody jobRequest jobRequest);

    @PutMapping("/api/jobs/{id}")
    ResponseEntity<JobDTO> updateJob(@PathVariable("id") String id, @RequestBody JobDTO job);

    @DeleteMapping("/api/jobs/{id}")
    ResponseEntity<Void> deleteJob(@PathVariable("id") String id);

    // HR job management endpoints
    @GetMapping("/api/jobs/my-jobs")
    ResponseEntity<PageableResponse<JobDTO>> getMyJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );

    @GetMapping("/api/jobs/my-jobs/status/{status}")
    ResponseEntity<PageableResponse<JobDTO>> getMyJobsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    );
}
