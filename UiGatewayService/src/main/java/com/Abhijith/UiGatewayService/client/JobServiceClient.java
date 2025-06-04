package com.Abhijith.UiGatewayService.client;

import com.Abhijith.UiGatewayService.dto.jobRequest;
import com.Abhijith.UiGatewayService.model.Job;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "job-posting-service")
public interface JobServiceClient {

	@PostMapping("/jobs")
	Job createJob(@RequestBody jobRequest jobRequest);

	@GetMapping("/jobs")
	List<Job> getAllJobs();
	
	@GetMapping("/jobs/{id}")
	Job getJobById(@PathVariable("id") String id);
}
