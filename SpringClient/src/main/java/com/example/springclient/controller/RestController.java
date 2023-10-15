package com.example.springclient.controller;

import static org.awaitility.Awaitility.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.ResponseEntity.badRequest;
import com.example.springclient.entity.Adresse;
import com.example.springclient.service.ConsumerService;
import com.example.springclient.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.UUID;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * Controller Klasse.
 */
@Slf4j
@Controller
@RequestMapping("/rest/solarapi")
@RequiredArgsConstructor
public class RestController {

    private final KafkaConnectionCheck checker;
    private final ConsumerService consumerService;
    private final ProducerService producerService;

    /**
     * Methode, die Adresse aus Anfrage entgegennimmt.
     * @param adresse Adresse, die an den Message Broker gesendet werden soll.
     * @return Http Status.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<String> post(@RequestBody Adresse adresse){
        log.debug("post: Adresse");

        if(adresse == null || !checker.isKafkaServerReachable()){
            log.error("post: Adresse ungültig oder Server nicht erreichbar!");
            return badRequest().build();
        }

        adresse.setId(UUID.randomUUID());
        UUID idAdresse = adresse.getId();

        producerService.sendeNachricht(adresse.toJson());

        try{
            await().atMost(100000,SECONDS).until(()->consumerService.getEmpfangeneAdresse() != null);
        }catch(Exception ex){
            log.error("Gateway Timeout");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Timeout beim Warten auf die Antwort");
        }

        UUID id = consumerService.getEmpfangeneAdresse().getId();
        String ergebnis = consumerService.getEmpfangeneAdresse().getWattNextDay();
        int hnr = consumerService.getEmpfangeneAdresse().getHausnummer();
        log.debug("Check Id: {} == Id diese Adresse: {}", id, idAdresse);
        if(idAdresse.equals(id)){
            log.debug("Solaranlage: {} Wh",ergebnis);
            consumerService.setEmpfangeneAdresse(null);
            return ResponseEntity.status(HttpStatus.OK).body(ergebnis);
        }

        consumerService.setEmpfangeneAdresse(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler!");
    }
}
