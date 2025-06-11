package com.Abhijith.UiGatewayService.service;

import com.Abhijith.UiGatewayService.client.JobServiceClient;
import com.Abhijith.UiGatewayService.dto.JobDTO;
import com.Abhijith.UiGatewayService.dto.PageableResponse;
import com.Abhijith.UiGatewayService.dto.jobRequest;
import com.Abhijith.UiGatewayService.exception.JobNotFoundException;
import com.Abhijith.UiGatewayService.exception.JobServiceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {

    private final JobServiceClient jobServiceClient;

    public PageableResponse<JobDTO> getAllJobs(int page, int size, String sortBy, String sortDir) {
        try {
            return jobServiceClient.getAllJobs(page, size, sortBy, sortDir).getBody();
        } catch (FeignException e) {
            log.error("Error fetching all jobs: {}", e.getMessage());
            throw new JobServiceException("Failed to fetch jobs", e);
        }
    }

    public JobDTO getJobById(String id) {
        try {
            JobDTO job = jobServiceClient.getJobById(id).getBody();
            if (job == null) {
                throw new JobNotFoundException("Job not found with id: " + id);
            }
            return job;
        } catch (FeignException.NotFound e) {
            throw new JobNotFoundException("Job not found with id: " + id);
        } catch (FeignException e) {
            log.error("Error fetching job with id {}: {}", id, e.getMessage());
            throw new JobServiceException("Failed to fetch job details", e);
        }
    }

    public PageableResponse<JobDTO> getMyJobs(int page, int size, String sortBy, String sortDir) {
        try {
            return jobServiceClient.getMyJobs(page, size, sortBy, sortDir).getBody();
        } catch (FeignException e) {
            log.error("Error fetching user's jobs: {}", e.getMessage());
            throw new JobServiceException("Failed to fetch your jobs", e);
        }
    }

    public PageableResponse<JobDTO> getMyJobsByStatus(String status, int page, int size, String sortBy, String sortDir) {
        try {
            return jobServiceClient.getMyJobsByStatus(status, page, size, sortBy, sortDir).getBody();
        } catch (FeignException e) {
            log.error("Error fetching jobs by status {}: {}", status, e.getMessage());
            throw new JobServiceException("Failed to fetch jobs by status", e);
        }
    }

    public JobDTO createJob(jobRequest request) {
        try {
            JobDTO createdJob = jobServiceClient.createJob(request).getBody();
            if (createdJob == null) {
                throw new JobServiceException("Failed to create job - no response from service");
            }
            return createdJob;
        } catch (FeignException e) {
            log.error("Error creating job: {}", e.getMessage());
            throw new JobServiceException("Failed to create job", e);
        }
    }

    public JobDTO updateJob(String id, JobDTO jobDTO) {
        try {
            JobDTO updatedJob = jobServiceClient.updateJob(id, jobDTO).getBody();
            if (updatedJob == null) {
                throw new JobNotFoundException("Job not found with id: " + id);
            }
            return updatedJob;
        } catch (FeignException.NotFound e) {
            throw new JobNotFoundException("Job not found with id: " + id);
        } catch (FeignException e) {
            log.error("Error updating job {}: {}", id, e.getMessage());
            throw new JobServiceException("Failed to update job", e);
        }
    }

    public boolean deleteJob(String id) {
        try {
            jobServiceClient.deleteJob(id);
            return true;
        } catch (FeignException.NotFound e) {
            throw new JobNotFoundException("Job not found with id: " + id);
        } catch (FeignException e) {
            log.error("Error deleting job {}: {}", id, e.getMessage());
            throw new JobServiceException("Failed to delete job", e);
        }
    }
}
