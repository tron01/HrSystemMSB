package com.Abhijith.ApplicationService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeParseRequest {
	private String applicantName;
	private String resumeUrl;
	private String jobId;
	private String email;
}
