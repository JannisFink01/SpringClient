package com.example.springclient.controller;

import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Klasse zur Prüfung, ob Apache Kafka Server erreichbar ist.
 */
@Configuration
public class KafkaConnectionCheck {
    /**
     * Methode, die Verbindung zu dem Socket des Apache Kafka Servers aufbaut.
     * @return true, wenn Server erreichbar ist und false, wenn Server nicht erreichbar ist.
     */
    public boolean isKafkaServerReachable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 9092), 5000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
