package org.example.serious.state.service.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.serious.state.service.model.Document;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DocumentCheckListener {

    @RabbitListener(queues = "document-check-queue")
    public void receiveDocument(Document document) {
        log.info("Received: [{}]", document);
    }
}
