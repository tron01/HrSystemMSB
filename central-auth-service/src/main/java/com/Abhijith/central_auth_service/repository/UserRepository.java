package com.Abhijith.central_auth_service.repository;

import com.Abhijith.central_auth_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}

