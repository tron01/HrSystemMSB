package com.Abhijith.ApplicationService.feign;

import com.Abhijith.ApplicationService.dto.ResumeParseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "resume-parser-service")
public interface ResumeParserClient {

	@PostMapping("/resume-parser/parse")
	ResponseEntity<String> sendResumeForParsing(@RequestBody ResumeParseRequest request);

}
