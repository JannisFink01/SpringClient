package com.example.springclient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service Klasse.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    public void sendeNachricht(String adresse){
        log.debug("sendeNachricht: {}", adresse);
        kafkaTemplate.send("solarapi",adresse);
    }
}
