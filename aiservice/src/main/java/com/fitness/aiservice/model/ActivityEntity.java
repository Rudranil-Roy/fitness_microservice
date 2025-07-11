package com.fitness.aiservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityEntity {
    private String id;
    private String userId;
    private Integer duration;
    private String type;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
