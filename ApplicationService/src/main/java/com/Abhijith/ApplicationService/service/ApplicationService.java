package com.Abhijith.ApplicationService.service;

import com.Abhijith.ApplicationService.dto.ApplicationDto;
import com.Abhijith.ApplicationService.model.Application;
import com.Abhijith.ApplicationService.repository.ApplicationRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private Cloudinary cloudinary;
    
    public ApplicationDto createApplication(String jobId, String studentName, String email, MultipartFile resumeFile) throws IOException {
        // Upload file to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(resumeFile.getBytes(),
                ObjectUtils.asMap("resource_type", "raw"));
        
        String resumeUrl = uploadResult.get("secure_url").toString();
        
        // Create and save application
        Application application = new Application();
        application.setJobId(jobId);
        application.setStudentName(studentName);
        application.setEmail(email);
        application.setResumeUrl(resumeUrl);
        
        Application savedApplication = applicationRepository.save(application);
        
        // Convert to DTO
        ApplicationDto applicationDto = new ApplicationDto();
        BeanUtils.copyProperties(savedApplication, applicationDto);
        
        return applicationDto;
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
