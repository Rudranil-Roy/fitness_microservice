package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.entity.UserEntity;
import com.fitness.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            UserEntity existingUser= userRepository.getUserEntitiesByEmail();
            UserResponse response = new UserResponse();
            response.setId(existingUser.getId());
            response.setKeyCloakId(existingUser.getKeyCloakId());
            response.setEmail(existingUser.getEmail());
            response.setPassword(existingUser.getPassword());
            response.setFirstName(existingUser.getFirstName());
            response.setLastName(existingUser.getLastName());
            response.setCreatedAt(existingUser.getCreatedAt());
            response.setUpdatedAt(existingUser.getUpdatedAt());
            return response;
        }

        UserEntity user= new UserEntity();
        user.setEmail(request.getEmail());
        user.setKeyCloakId(request.getKeyCloakId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());

        user= userRepository.save(user);

        UserResponse response= new UserResponse();
        response.setId(user.getId());
        response.setKeyCloakId(user.getKeyCloakId());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public UserResponse getUserProfile(String userId) {

        return (UserResponse) userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Boolean existByUserId(String userId) {
        return userRepository.existsByKeyCloakId
                (userId);
    }
}
