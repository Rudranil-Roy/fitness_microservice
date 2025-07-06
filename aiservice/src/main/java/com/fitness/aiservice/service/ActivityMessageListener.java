package com.fitness.aiservice.service;

import com.fitness.aiservice.model.ActivityEntity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;

    private final ActivityAiService activityAiService;
    private final RecommendationRepo recommendationRepo;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(ActivityEntity activityEntity) {
        try {
            log.info("Received activity message from activity entity: {}", activityEntity.getId());
//            log.info("Recommendation Genetated Succecfully: {}", activityAiService.generateRecommendation(activityEntity));
        recommendationRepo.save(activityAiService.generateRecommendation(activityEntity));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
