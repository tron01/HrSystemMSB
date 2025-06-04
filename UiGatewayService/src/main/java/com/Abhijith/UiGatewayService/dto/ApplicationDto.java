package com.Abhijith.UiGatewayService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationDto {
	private String id;
	private String jobId;
	private String studentName;
	private String email;
	private String resumeUrl;
}