package com.Abhijith.UiGatewayService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.Abhijith.UiGatewayService.client.ResumeParserClient;
import com.Abhijith.UiGatewayService.dto.ParsedResume;
import java.util.List;

@Controller
@RequestMapping("/resume-parser")
public class ResumeParserController {
    @Autowired
    private ResumeParserClient resumeParserClient;

    @GetMapping("")
    public String listParsedResumes(Model model) {
        List<ParsedResume> resumes = resumeParserClient.getAllParsedResumes();
        model.addAttribute("resumes", resumes);
        return "parsed_resumes";
    }

    @GetMapping("/{id}")
    public String getParsedResumeById(@PathVariable("id") String id, Model model) {
        ParsedResume resume = resumeParserClient.getParsedResumeById(id);
        model.addAttribute("parsedResume", resume);
        return "parsed_resume";
    }
}
