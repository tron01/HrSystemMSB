package com.Abhijith.UiGatewayService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private String id;
    private String title;
    private String description;
    private String location;
    private String postedBy;
    private String companyName;
    private String employmentType;
    private String salaryRange;
    private String skillsRequired;
    private String experienceLevel;
    private String jobCategory;
    private String jobType;
    private String applicationDeadline;
    private String status;
    private String createdAt;
    private String updatedAt;
}
