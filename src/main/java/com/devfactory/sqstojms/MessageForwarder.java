package com.devfactory.sqstojms;

import org.apache.activemq.ActiveMQConnectionFactory;
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

    private final JmsTemplate _jmsTemplate;

    @Value("${jms.outgoing.queue}")
    private String _queue;

    public MessageForwarder(@Autowired ActiveMQConnectionFactory connectionFactory) {
        _jmsTemplate = new JmsTemplate(connectionFactory);
    }

    @JmsListener(destination = "${sqs.incoming.queue}")
    public void process(String messageJson) {
        System.out.println(messageJson);
        _jmsTemplate.send(_queue, session -> session.createTextMessage(messageJson));
    }
}
