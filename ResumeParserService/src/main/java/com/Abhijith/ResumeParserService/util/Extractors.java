package com.Abhijith.ResumeParserService.util;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Extractors {

	public String extractName(String text) {
		String[] lines = text.split("\\r?\\n");
		for (String line : lines) {
			line = line.trim();
			if (!line.toLowerCase().contains("email") && !line.toLowerCase().contains("phone") &&
					    !line.toLowerCase().contains("github") && !line.toLowerCase().contains("linkedin") &&
					    !line.isEmpty() && line.length() <= 40) {
				return line;
			}
		}
		return null;
	}
	
	public String extractEmail(String text) {
		Matcher matcher = Pattern.compile("[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}").matcher(text);
		return matcher.find() ? matcher.group() : null;
	}
	
	public String extractPhone(String text) {
		Matcher matcher = Pattern.compile("\\+?\\d[\\d\\s\\-()]{9,}\\d").matcher(text);
		return matcher.find() ? matcher.group().replaceAll("[^\\d+]", "") : null;
	}
	
	public String extractLinkedIn(String text) {
		Matcher matcher = Pattern.compile("linkedin\\.com/in/\\S+").matcher(text);
		return matcher.find() ? "https://" + matcher.group() : null;
	}
	
	public String extractGitHub(String text) {
		Matcher matcher = Pattern.compile("github\\.com/\\S+").matcher(text);
		return matcher.find() ? "https://" + matcher.group() : null;
	}
	
	public List<String> extractSkills(String text) {
		List<String> skills = new ArrayList<>();
		Matcher matcher = Pattern.compile("(?i)Technical Skills\\s*[:\\n]*(.*?)\\n\\s*\\n", Pattern.DOTALL).matcher(text);
		if (matcher.find()) {
			String section = matcher.group(1);
			String[] lines = section.split("\\n");
			for (String line : lines) {
				if (line.contains(":")) {
					String[] parts = line.split(":", 2);
					String[] items = parts[1].split(",");
					for (String item : items) {
						String skill = item.trim();
						if (!skill.isEmpty()) {
							skills.add(skill);
						}
					}
				}
			}
		}
		return skills;
	}
	
	public String extractWorkExperience(String text) {
		Matcher matcher = Pattern.compile("(?i)Work Experience\\s*\\n+(.*?)\\n\\s*\\n", Pattern.DOTALL).matcher(text);
		return matcher.find() ? matcher.group(1).trim() : null;
	}
	
	public String extractEducation(String text) {
		Matcher matcher = Pattern.compile("(?i)Education\\s*\\n+(.*?)\\n\\s*\\n", Pattern.DOTALL).matcher(text);
		return matcher.find() ? matcher.group(1).trim() : null;
	}
	
	public String extractCertifications(String text) {
		Matcher matcher = Pattern.compile("(?i)Certifications.*?\\n+(.*?)\\n\\s*\\n", Pattern.DOTALL).matcher(text);
		return matcher.find() ? matcher.group(1).trim() : null;
	}
	
	public String extractProjects(String text) {
		Matcher matcher = Pattern.compile("(?i)Projects.*?\\n+(.*?)\\n\\s*\\n", Pattern.DOTALL).matcher(text);
		return matcher.find() ? matcher.group(1).trim() : null;
	}
	
	public String extractLanguages(String text) {
		Matcher matcher = Pattern.compile("(?i)Languages\\s*[:\\-]?\\s*(.*)").matcher(text);
		return matcher.find() ? matcher.group(1).trim() : null;
	}
	
	
	public List<Map<String, String>> extractEducationDetails(String text) {
		List<Map<String, String>> educationList = new ArrayList<>();
		
		Pattern pattern = Pattern.compile("(?i)(Bachelor|Master|B\\.Tech|M\\.Tech|MCA|BCA|BSc|MSc).*?\\n(.*?)\\n", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		
		while (matcher.find()) {
			Map<String, String> edu = new HashMap<>();
			edu.put("degree", matcher.group(1).trim());
			edu.put("university", matcher.group(2).trim());
			
			// Optionally extract year if available after university
			Pattern yearPattern = Pattern.compile("(\\d{4})");
			Matcher yearMatcher = yearPattern.matcher(matcher.group());
			if (yearMatcher.find()) {
				edu.put("year", yearMatcher.group(1));
			}
			
			educationList.add(edu);
		}
		
		return educationList;
	}
	
	public Map<String, Object> extractAll(String text) {
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("name", extractName(text));
		result.put("email", extractEmail(text));
		result.put("phone", extractPhone(text));
		result.put("linkedin", extractLinkedIn(text));
		result.put("github", extractGitHub(text));
		result.put("skills", extractSkills(text));
		result.put("workExperience", extractWorkExperience(text));
		result.put("education", extractEducation(text));
		result.put("educationDetails", extractEducationDetails(text));
		result.put("certifications", extractCertifications(text));
		result.put("projects", extractProjects(text));
		result.put("languages", extractLanguages(text));
		return result;
	}
}
