package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.entity.ActivityEntity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.routing.key}")
    private String routingKey;

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    public ActivityResponse trackActivity(ActivityRequest request) {
        Boolean isValidUser= userValidationService.validateUser(request.getUserId());
        if(!isValidUser){
            throw new RuntimeException("Invalid User "+ request.getUserId());
        }

        ActivityEntity activity= ActivityEntity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .additionalMetrics(request.getAdditionalMetrics())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .duration(request.getDuration())
                .build();

        activity= activityRepository.save(activity);

        // push to rabbitmq for ai processing
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, activity);
        } catch (Exception e){
            log.error("Unable to send activity event",e);
        }

        ActivityResponse activityResponse = getActivityResponse(activity);

        return activityResponse;
    }

    private ActivityResponse getActivityResponse(ActivityEntity activity) {
        ActivityResponse activityResponse= new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setType(activity.getType());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());
        return activityResponse;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<ActivityEntity> activities= activityRepository.findByUserId(userId);
        return activities.stream()
                .map(this::getActivityResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String id) {
        return activityRepository.findById(id)
                .map(this::getActivityResponse)
                .orElseThrow(() -> new RuntimeException("Activity not found"+ id));
    }
}
