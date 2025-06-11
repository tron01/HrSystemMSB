package com.Abhijith.JobPostingService.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jobs")
@Getter
@Setter
public class Job {
	@Id
	private String id;
	private String title;
	private String description;
	private String location;
	private String postedBy; // User name of the person who posted the job
	private String companyName;
	private String employmentType; // e.g., Full-time, Part-time, Contract
	private String salaryRange; // e.g., "50000-70000"
	private String skillsRequired; // Comma-separated list of skills
	private String experienceLevel; // e.g., "Entry-level", "Mid-level", "Senior"
	private String jobCategory; // e.g., "Software Development", "Data Science", "Marketing"
	private String jobType;
	private String applicationDeadline; // e.g., "2023-12-31"
	private String status; // e.g., "Open", "Closed", "Pending"
	private String createdAt; // Timestamp of job posting creation
	private String updatedAt; // Timestamp of last update to the job posting
}
