package com.Abhijith.UiGatewayService.controller;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.Abhijith.UiGatewayService.feign.ApplicationServiceClient;
import com.Abhijith.UiGatewayService.dto.ApplicationDto;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/applications")
public class ApplicationController {
    @Autowired
    private ApplicationServiceClient applicationServiceClient;

    @GetMapping
    public String listApplications(Model model) {
        List<ApplicationDto> applications = applicationServiceClient.getAllApplications();
        model.addAttribute("applications", applications);
        return "applications";
    }

    @GetMapping("/new")
    public String showCreateApplicationForm(Model model) {
        model.addAttribute("applicationDto", new ApplicationDto());
        return "application_form";
    }

    @PostMapping()
    public String createApplication(@RequestParam("jobId") String jobId,
                                    @RequestParam("studentName") String studentName,
                                    @RequestParam("email") String email,
                                    @RequestParam("resumeFile") MultipartFile resumeFile) {
        applicationServiceClient.createApplication(jobId, studentName, email, resumeFile);
        return "redirect:/api/v1/applications";
    }

    @GetMapping("/{id}")
    public String getApplicationById(@PathVariable("id") String id, Model model) {
        ApplicationDto application = applicationServiceClient.getApplicationById(id);
        Map<String, String> resumeUrlMap = applicationServiceClient.getResumeUrl(id);
        String resumeUrl = resumeUrlMap != null ? resumeUrlMap.get("url") : null;
        model.addAttribute("a", application);
        model.addAttribute("resumeUrl", resumeUrl);
        return "application";
    }

    @GetMapping("/view-resume")
    public ResponseEntity<byte[]> viewResume(@RequestParam String url) {
        try {
            URL pdfUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) pdfUrl.openConnection();
            connection.setRequestMethod("GET");
            
            try (InputStream inputStream = connection.getInputStream()) {
                byte[] pdfBytes = inputStream.readAllBytes();
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.inline().filename("resume.pdf").build());
                
                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
}
