package com.Abhijith.ResumeParserService.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "parsed_resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedResume {
	@Id
	private String id;
	
	private String applicantName;
	private String email;
	private String jobId;
	
	private String name;
	private String phone;
	private String linkedin;
	private String github;
	
	private List<String> skills;
	private String workExperience;
	private String education;
	private List<Map<String, String>> educationDetails;
	private String certifications;
	private String projects;
	private String languages;
	
	private LocalDateTime parsedAt;
}
