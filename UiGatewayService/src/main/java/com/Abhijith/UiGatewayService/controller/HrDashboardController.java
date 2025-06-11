package com.Abhijith.UiGatewayService.controller;

import com.Abhijith.UiGatewayService.service.JobService;
import com.Abhijith.UiGatewayService.dto.JobDTO;
import com.Abhijith.UiGatewayService.dto.PageableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/hr")
@PreAuthorize("hasRole('HR')")
public class HrDashboardController {

    private final JobService jobService;

    @Autowired
    public HrDashboardController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            Authentication auth) {
        try {
            // Fetch recent jobs posted by the HR
            PageableResponse<JobDTO> myJobsPage = jobService.getMyJobs(page, size, "createdAt", "desc");
            
            if (myJobsPage != null) {
                // Get all jobs for statistics
                PageableResponse<JobDTO> allJobs = jobService.getAllJobs(0, 1000, "createdAt", "desc");
                
                // Calculate statistics
                long totalJobsCount = myJobsPage.getTotalElements();
                long activeJobsCount = allJobs.getContent().stream()
                        .filter(job -> "Open".equals(job.getStatus()))
                        .count();
                long closedJobsCount = allJobs.getContent().stream()
                        .filter(job -> "Closed".equals(job.getStatus()))
                        .count();
                
                // Calculate jobs by category
                Map<String, Long> jobsByCategory = allJobs.getContent().stream()
                        .collect(Collectors.groupingBy(
                            JobDTO::getJobCategory,
                            Collectors.counting()
                        ));
                
                // Get jobs with approaching deadlines (within next 7 days)
                LocalDate now = LocalDate.now();
                LocalDate sevenDaysLater = now.plusDays(7);
                long urgentDeadlines = allJobs.getContent().stream()
                        .filter(job -> {
                            try {
                                LocalDate deadline = LocalDate.parse(job.getApplicationDeadline());
                                return deadline.isAfter(now) && deadline.isBefore(sevenDaysLater);
                            } catch (Exception e) {
                                log.error("Error parsing deadline date: {}", e.getMessage());
                                return false;
                            }
                        })
                        .count();

                // Add data to model
                model.addAttribute("activeJobsCount", activeJobsCount);
                model.addAttribute("totalJobsCount", totalJobsCount);
                model.addAttribute("closedJobsCount", closedJobsCount);
                model.addAttribute("urgentDeadlines", urgentDeadlines);
                model.addAttribute("jobsByCategory", jobsByCategory);
                model.addAttribute("recentJobs", myJobsPage.getContent());
                model.addAttribute("currentPage", myJobsPage.getPageNo());
                model.addAttribute("totalPages", myJobsPage.getTotalPages());
                model.addAttribute("totalItems", myJobsPage.getTotalElements());
                
                // Add current date for deadline comparisons
                model.addAttribute("currentDate", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
            }
            
            return "hr/dashboard";
        } catch (Exception e) {
            log.error("Error loading dashboard: {}", e.getMessage());
            model.addAttribute("error", "Unable to fetch job data: " + e.getMessage());
            model.addAttribute("activeJobsCount", 0);
            model.addAttribute("totalJobsCount", 0);
            model.addAttribute("closedJobsCount", 0);
            model.addAttribute("urgentDeadlines", 0);
            return "hr/dashboard";
        }
    }
}
