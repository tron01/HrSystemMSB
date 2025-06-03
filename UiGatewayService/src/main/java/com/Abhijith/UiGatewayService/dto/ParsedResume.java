package com.Abhijith.UiGatewayService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedResume {
	private String id;
	private String applicantName;
	private String email;
	private String phone;
	private String github;
	private String linkedin;
	private List<String> skills;
	private String education;
	private String workExperience;
	private String projects;
	private String certifications;
	private String languages;
	private List<Map<String, String>> educationDetails;
}
