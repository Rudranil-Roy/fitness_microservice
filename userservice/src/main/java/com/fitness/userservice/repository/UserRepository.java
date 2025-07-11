package com.fitness.userservice.repository;

import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByEmail( String email);

    Boolean existsByKeycloakId(String userId);

    UserEntity getUserEntitiesByEmail(String email);
}
