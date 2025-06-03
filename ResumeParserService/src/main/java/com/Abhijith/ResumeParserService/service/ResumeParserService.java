package com.Abhijith.ResumeParserService.service;

import com.Abhijith.ResumeParserService.dto.ResumeParseRequest;
import com.Abhijith.ResumeParserService.model.ParsedResume;
import com.Abhijith.ResumeParserService.repository.ParsedResumeRepository;
import com.Abhijith.ResumeParserService.util.Extractors;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeParserService {

	private final Extractors extractors;
	private final ParsedResumeRepository parsedResumeRepository;
	
	public void parseAndStore(ResumeParseRequest request) throws IOException {
		
		try (InputStream input = new URL(request.getResumeUrl()).openStream();
		     PDDocument document = PDDocument.load(input)) {
			
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);
			
			Map<String, Object> extracted = extractors.extractAll(text);
			
			ParsedResume resume = ParsedResume.builder()
					                      .applicantName(request.getApplicantName())
					                      .email(request.getEmail())
					                      .jobId(request.getJobId())
					                      .name((String) extracted.get("name"))
					                      .phone((String) extracted.get("phone"))
					                      .linkedin((String) extracted.get("linkedin"))
					                      .github((String) extracted.get("github"))
					                      .skills((List<String>) extracted.get("skills"))
					                      .workExperience((String) extracted.get("workExperience"))
					                      .education((String) extracted.get("education"))
					                      .educationDetails((List<Map<String, String>>) extracted.get("educationDetails"))
					                      .certifications((String) extracted.get("certifications"))
					                      .projects((String) extracted.get("projects"))
					                      .languages((String) extracted.get("languages"))
					                      .parsedAt(LocalDateTime.now())
					                      .build();
			
			parsedResumeRepository.save(resume);
		}
	}

	public List<ParsedResume> getAllParsedResumes() {
		return parsedResumeRepository.findAll();
	}
	
	public ParsedResume getParsedResumeById(String id) {
		return parsedResumeRepository.findById(id).orElse(null);
	}
}

