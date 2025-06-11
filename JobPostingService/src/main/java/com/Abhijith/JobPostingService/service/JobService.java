package com.Abhijith.JobPostingService.service;


import com.Abhijith.JobPostingService.dto.PageableResponse;
import com.Abhijith.JobPostingService.model.Job;
import com.Abhijith.JobPostingService.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository repo;
    
    public Job create(Job j) {
        String now = Instant.now().toString();
        j.setCreatedAt(now);
        j.setUpdatedAt(now);
        j.setStatus("Open");
        return repo.save(j);
    }
    
    public List<Job> all() {
        return repo.findAll();
    }
    
    public PageableResponse<Job> findAll(Pageable pageable) {
        Page<Job> page = repo.findAll(pageable);
        return new PageableResponse<>(page);
    }
    
    public PageableResponse<Job> findByPostedBy(String postedBy, Pageable pageable) {
        Page<Job> page = repo.findByPostedBy(postedBy, pageable);
        return new PageableResponse<>(page);
    }
    
    public PageableResponse<Job> findByPostedByAndStatus(String postedBy, String status, Pageable pageable) {
        Page<Job> page = repo.findByPostedByAndStatus(postedBy, status, pageable);
        return new PageableResponse<>(page);
    }
    
    public Job byId(String id) {
        return repo.findById(id).orElse(null);
    }
    
    public Job update(String id, Job j) {
        Job existing = byId(id);
        if (existing == null) return null;
        j.setId(id);
        j.setCreatedAt(existing.getCreatedAt());
        j.setUpdatedAt(Instant.now().toString());
        return repo.save(j);
    }
    
    public void delete(String id) {
        repo.deleteById(id);
    }
}
