package com.Abhijith.UiGatewayService.controller;

import com.Abhijith.UiGatewayService.dto.JobDTO;
import com.Abhijith.UiGatewayService.dto.PageableResponse;
import com.Abhijith.UiGatewayService.exception.JobNotFoundException;
import com.Abhijith.UiGatewayService.service.JobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import com.Abhijith.UiGatewayService.dto.jobRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/hr/jobs")
@PreAuthorize("hasRole('HR')")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/my-jobs")
    public String listMyJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {
        try {
            PageableResponse<JobDTO> jobPage = jobService.getMyJobs(page, size, sortBy, sortDir);
            model.addAttribute("jobs", jobPage.getContent());
            model.addAttribute("currentPage", jobPage.getPageNo());
            model.addAttribute("totalPages", jobPage.getTotalPages());
            model.addAttribute("totalItems", jobPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            return "hr/jobs/my-jobs";
        } catch (Exception e) {
            log.error("Error fetching jobs: {}", e.getMessage());
            model.addAttribute("error", "Failed to fetch jobs. Please try again later.");
            return "hr/jobs/my-jobs";
        }
    }

    @GetMapping("/my-jobs/status/{status}")
    public String listMyJobsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {
        try {
            PageableResponse<JobDTO> jobPage = jobService.getMyJobsByStatus(status, page, size, sortBy, sortDir);
            model.addAttribute("jobs", jobPage.getContent());
            model.addAttribute("currentPage", jobPage.getPageNo());
            model.addAttribute("totalPages", jobPage.getTotalPages());
            model.addAttribute("totalItems", jobPage.getTotalElements());
            model.addAttribute("currentStatus", status);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            return "hr/jobs/my-jobs";
        } catch (Exception e) {
            log.error("Error fetching jobs by status: {}", e.getMessage());
            model.addAttribute("error", "Failed to fetch jobs. Please try again later.");
            return "hr/jobs/my-jobs";
        }
    }

    @GetMapping("/new")
    public String showCreateJobForm(Model model) {
        model.addAttribute("job", new jobRequest());
        addCommonAttributes(model);
        return "hr/jobs/create";
    }

    @PostMapping
    public String createJob(@ModelAttribute jobRequest jobRequest, Authentication auth, Model model) {
        try {
            jobRequest.setPostedBy(auth.getName());
            JobDTO createdJob = jobService.createJob(jobRequest);
            return "redirect:/hr/jobs/my-jobs";
        } catch (Exception e) {
            log.error("Error creating job: {}", e.getMessage());
            model.addAttribute("error", "Failed to create job. Please try again.");
            model.addAttribute("job", jobRequest);
            addCommonAttributes(model);
            return "hr/jobs/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditJobForm(@PathVariable String id, Model model) {
        try {
            JobDTO job = jobService.getJobById(id);
            if (job == null) {
                model.addAttribute("error", "Job not found");
                return "redirect:/hr/jobs/my-jobs";
            }
            model.addAttribute("job", job);
            addCommonAttributes(model);
            return "hr/jobs/edit";
        } catch (Exception e) {
            log.error("Error fetching job for edit: {}", e.getMessage());
            model.addAttribute("error", "Failed to fetch job details");
            return "redirect:/hr/jobs/my-jobs";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateJob(@PathVariable String id, @ModelAttribute JobDTO job, Model model) {
        try {
            JobDTO updatedJob = jobService.updateJob(id, job);
            if (updatedJob == null) {
                model.addAttribute("error", "Job not found");
                return "redirect:/hr/jobs/my-jobs";
            }
            return "redirect:/hr/jobs/my-jobs";
        } catch (Exception e) {
            log.error("Error updating job: {}", e.getMessage());
            model.addAttribute("error", "Failed to update job");
            model.addAttribute("job", job);
            addCommonAttributes(model);
            return "hr/jobs/edit";
        }
    }

    @GetMapping("/{id}")
    public String viewJob(@PathVariable String id, Model model) {
        try {
            JobDTO job = jobService.getJobById(id);
            if (job == null) {
                model.addAttribute("error", "Job not found");
                return "redirect:/hr/jobs/my-jobs";
            }
            model.addAttribute("job", job);
            return "hr/jobs/view";
        } catch (Exception e) {
            log.error("Error viewing job: {}", e.getMessage());
            model.addAttribute("error", "Failed to fetch job details");
            return "redirect:/hr/jobs/my-jobs";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteJob(@PathVariable String id, Model model) {
        try {
            boolean deleted = jobService.deleteJob(id);
            if (!deleted) {
                model.addAttribute("error", "Job not found");
            }
            return "redirect:/hr/jobs/my-jobs";
        } catch (Exception e) {
            log.error("Error deleting job: {}", e.getMessage());
            model.addAttribute("error", "Failed to delete job");
            return "redirect:/hr/jobs/my-jobs";
        }
    }

    private void addCommonAttributes(Model model) {
        model.addAttribute("employmentTypes",
                new String[]{"Full-time", "Part-time", "Contract", "Internship"});
        model.addAttribute("experienceLevels",
                new String[]{"Entry-level", "Mid-level", "Senior", "Executive"});
        model.addAttribute("jobCategories",
                new String[]{"Software Development", "Data Science", "Marketing", "Sales", "HR", "Finance"});
    }
}
