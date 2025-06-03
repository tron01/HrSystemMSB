package com.Abhijith.ResumeParserService.repository;

import com.Abhijith.ResumeParserService.model.ParsedResume;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParsedResumeRepository extends MongoRepository<ParsedResume, String> {
}
