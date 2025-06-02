package com.Abhijith.JobPostingService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class jobRequest {
    private String title;
    private String description;
    private String location;
    private String postedBy;
}
