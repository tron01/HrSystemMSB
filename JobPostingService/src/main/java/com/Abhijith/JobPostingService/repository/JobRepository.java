package com.Abhijith.JobPostingService.repository;

import com.Abhijith.JobPostingService.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String> {
}
