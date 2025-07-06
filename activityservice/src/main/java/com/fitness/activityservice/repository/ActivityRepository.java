package com.fitness.activityservice.repository;

import com.fitness.activityservice.entity.ActivityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<ActivityEntity, String> {

    List<ActivityEntity> findByUserId(String userId);
}
