package com.devfactory.sqstojms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Listens to SQS queue and forwards same message to JMS
 */
@Service
@SuppressWarnings("unused")
public class MessageForwarder {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JmsTemplate _jmsTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${jms.queue}")
    private String _queue;

    public MessageForwarder(@Autowired ActiveMQConnectionFactory connectionFactory) {
        _jmsTemplate = new JmsTemplate(connectionFactory);
    }

    @JmsListener(destination = "${sqs.queue}")
    public void process(String messageJson) {
        // Get message payload json from json
        String message;
        try {
            message = mapper.readTree(messageJson)
                    .path("Message")
                    .asText()
                    .replace("\n", "") // Replaces double escaped things
                    .replace("\\","");
        } catch (Exception ex) {
            log.error("Unable to parse json, message will be skipped {}", messageJson, ex);
            return;
        }

        _jmsTemplate.send(_queue, session -> session.createTextMessage(message));
        log.debug("Sent successfully to {}: {}", _queue, message);
    }
}
