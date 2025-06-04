package com.Abhijith.UiGatewayService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.Abhijith.UiGatewayService.client.JobServiceClient;
import com.Abhijith.UiGatewayService.model.Job;
import com.Abhijith.UiGatewayService.dto.jobRequest;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobServiceClient jobServiceClient;

    @GetMapping
    public String listJobs(Model model) {
        List<Job> jobs = jobServiceClient.getAllJobs();
        model.addAttribute("jobs", jobs);
        return "jobs";
    }

    @GetMapping("/new")
    public String showCreateJobForm(Model model) {
        model.addAttribute("jobRequest", new jobRequest());
        return "job_form";
    }

    @PostMapping
    public String createJob(@ModelAttribute jobRequest jobRequest) {
        jobServiceClient.createJob(jobRequest);
        return "redirect:/jobs";
    }

    @GetMapping("/{id}")
    public String getJobById(@PathVariable("id") String id, Model model) {
        Job job = jobServiceClient.getJobById(id);
        model.addAttribute("job", job);
        return "job";
    }
}
