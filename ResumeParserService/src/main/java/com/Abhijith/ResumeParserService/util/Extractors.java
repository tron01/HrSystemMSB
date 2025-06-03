package com.Abhijith.ResumeParserService.util;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Extractors {

	public String extractEmail(String text) {
		Pattern pattern = Pattern.compile("[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}");
		Matcher matcher = pattern.matcher(text);
		return matcher.find() ? matcher.group() : null;
	}


	public String extractPhone(String text) {
		Pattern pattern = Pattern.compile("\\+?\\d[\\d\\s\\-()]{9,}\\d");
		Matcher matcher = pattern.matcher(text);
		return matcher.find() ? matcher.group().replaceAll("[^\\d+]", "") : null;
	}


	public String extractName(String text) {
		String[] lines = text.split("\\r?\\n");
		for (String line : lines) {
			line = line.trim();
			if (!line.isEmpty() && !line.matches("(?i).*@(.*)|.*http.*|.*www.*")) {
				return line;
			}
		}
		return null;
	}


	public List<String> extractSkills(String text) {
		List<String> skills = new ArrayList<>();
		Pattern pattern = Pattern.compile("(?i)Technical Skills\\s*[:\\n]*(.*?)\\n\\s*\\n", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			String section = matcher.group(1);
			String[] lines = section.split("\\n");
			for (String line : lines) {
				if (line.contains(":")) {
					String[] parts = line.split(":", 2);
					String[] items = parts[1].split(",");
					for (String item : items) {
						item = item.trim();
						if (!item.isEmpty()) skills.add(item);
					}
				}
			}
		}
		return skills;
	}


	public String extractEducation(String text) {
		Pattern pattern = Pattern.compile("(?i)Education\\s*\\n+(.*?)\\n\\s*\\n", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return null;
	}

	public String extractWorkExperience(String text) {
		Pattern pattern = Pattern.compile("(?i)Work Experience\\s*\\n+(.*?)\\n\\s*\\n", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return null;
	}
	
}
