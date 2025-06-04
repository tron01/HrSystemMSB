package com.Abhijith.UiGatewayService.client;

import com.Abhijith.UiGatewayService.dto.ParsedResume;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "resume-parser-service")
public interface ResumeParserClient {

	@GetMapping("/parsed-data")
	List<ParsedResume> getAllParsedResumes();
	
	@GetMapping("/parsed-data/{id}")
	ParsedResume getParsedResumeById(@PathVariable("id") String id);
	
}
