package com.Abhijith.ApplicationService.service;

import com.Abhijith.ApplicationService.dto.ApplicationDto;
import com.Abhijith.ApplicationService.dto.ResumeParseRequest;
import com.Abhijith.ApplicationService.model.Application;
import com.Abhijith.ApplicationService.repository.ApplicationRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final Cloudinary cloudinary;
    private final WebClient webClient;
    
    public ApplicationDto createApplication(String jobId, String studentName, String email, MultipartFile resumeFile) throws IOException {
        // 1. Upload to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(resumeFile.getBytes(),
                ObjectUtils.asMap("resource_type", "raw"));
        String resumeUrl = uploadResult.get("secure_url").toString();
        
        // 2. Save application
        Application application = new Application();
        application.setJobId(jobId);
        application.setStudentName(studentName);
        application.setEmail(email);
        application.setResumeUrl(resumeUrl);
        
        Application saved = applicationRepository.save(application);
        
        // 3. Convert to DTO
        ApplicationDto dto = new ApplicationDto();
        BeanUtils.copyProperties(saved, dto);
        
        // 4. Send resume to resume-parser-service using WebClient
        ResumeParseRequest request = new ResumeParseRequest(studentName, resumeUrl, jobId, email);
        try {
            webClient.post()
                    .uri("http://localhost:8083/resume-parser/parse")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(); // async; use block() if you want sync
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        
        return dto;
    }
    
    public List<ApplicationDto> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(application -> {
                    ApplicationDto dto = new ApplicationDto();
                    BeanUtils.copyProperties(application, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public ApplicationDto getApplicationById(String id) {
        return applicationRepository.findById(id)
                .map(application -> {
                    ApplicationDto dto = new ApplicationDto();
                    BeanUtils.copyProperties(application, dto);
                    return dto;
                })
                .orElse(null);
    }
    
    public String getResumeUrlById(String id) {
        return applicationRepository.findById(id)
                       .map(Application::getResumeUrl)
                       .orElse(null);
    }
    
}
