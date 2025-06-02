package com.Abhijith.ApplicationService.repository;

import com.Abhijith.ApplicationService.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
}
