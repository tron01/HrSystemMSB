package com.Abhijith.UiGatewayService.client;

import com.Abhijith.UiGatewayService.dto.ApplicationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ApplicationService")
public interface ApplicationServiceClient {

	@PostMapping(path = "/applications",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ApplicationDto createApplication(
			@RequestParam("jobId") String jobId,
			@RequestParam("studentName") String studentName,
			@RequestParam("email") String email,
			@RequestParam("resumeFile") MultipartFile resumeFile);
	
	@GetMapping("/applications")
	List<ApplicationDto> getAllApplications();
	
	@GetMapping("/applications/{id}")
	ApplicationDto getApplicationById(@PathVariable("id") String id);
	
	@GetMapping("/applications/{id}/resume-url")
	Map<String, String> getResumeUrl(@PathVariable("id") String id);
	
}
