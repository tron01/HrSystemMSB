package com.Abhijith.ApplicationService.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applications")
@Getter
@Setter
public class Application {

	@Id
	private String id;
	private String jobId;
	private String studentName;
	private String email;
	private String resumeUrl;


}
