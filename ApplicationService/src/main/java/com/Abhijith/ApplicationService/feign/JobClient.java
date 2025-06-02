package com.Abhijith.ApplicationService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;


@FeignClient(name = "job-posting-service")
public interface JobClient {
    @GetMapping("/jobs")
    List<Map<String, Object>> getAllJobs();

    @GetMapping("/jobs/{id}")
    Map<String, Object> getJobById(@PathVariable("id") String id);
}
