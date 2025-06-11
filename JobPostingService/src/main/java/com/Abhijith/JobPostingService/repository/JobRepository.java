package com.Abhijith.JobPostingService.repository;

import com.Abhijith.JobPostingService.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByStatus(String status);
    List<Job> findByCompanyName(String companyName);
    List<Job> findByJobCategory(String category);
    List<Job> findByTitleContainingOrDescriptionContainingOrSkillsRequiredContaining(
        String title, String description, String skills);
    List<Job> findByPostedBy(String userId);
    List<Job> findByJobType(String jobType);
    List<Job> findByExperienceLevel(String experienceLevel);
    Page<Job> findByPostedBy(String postedBy, Pageable pageable);
    Page<Job> findByPostedByAndStatus(String postedBy, String status, Pageable pageable);
}
