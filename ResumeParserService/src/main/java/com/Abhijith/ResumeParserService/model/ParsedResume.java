package com.Abhijith.ResumeParserService.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parsed_resumes")
@Setter
@Getter
public class ParsedResume {
	@Id
	private String id;
	private String name;
	private String email;
	private String extractedText;
}
