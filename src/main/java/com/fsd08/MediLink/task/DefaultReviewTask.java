package com.fsd08.MediLink.task;

import com.fsd08.MediLink.service.AppointmentsService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DefaultReviewTask {

    public static Logger logger = LoggerFactory.getLogger(DefaultReviewTask.class);

    @Resource
    private AppointmentsService appointmentsService;

    /**
     * default review
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void doDefaultReview() {
        logger.info("execute default review task");
        appointmentsService.setDefaultReview();
    }

}
