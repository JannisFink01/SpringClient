package com.example.springclient.service;

import com.example.springclient.entity.Adresse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@Slf4j
@Service
@RequiredArgsConstructor
@Getter
public class ConsumerService {

    @Setter
    private Adresse empfangeneAdresse;

    @KafkaListener(topics = "solarResult", groupId = "group")
    public void nachrichtEmpfangen(String nachricht){
        log.debug("Empfangen: {}", nachricht);
        empfangeneAdresse = Adresse.toAdresse(nachricht);
        log.debug("adresse: {}", empfangeneAdresse);
    }
}
