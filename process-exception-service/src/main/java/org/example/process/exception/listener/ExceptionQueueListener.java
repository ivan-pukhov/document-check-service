package org.example.process.exception.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExceptionQueueListener {

    @RabbitListener(queues = "${process-exception-queue}")
    public void processResult(Object documentCheckRequest,
                              @Header(name = "x-death", required=false) List<Map<String, Object>> death) {
        log.info("Process exception for message: {}", documentCheckRequest);
        log.info("x-death: {}", death);
    }

}
