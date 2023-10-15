package com.example.springclient.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
@ToString
public class Adresse {
    private String strasse;
    private int hausnummer;
    private String plz;
    private String stadt;
    private String land;
    private int leistung;
    private String bundesland;
    private String wattNextDay;
    private Koordinaten koordinaten;
    @Setter
    private UUID id;

    public String toJson(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        }
        catch(JsonProcessingException e){
            log.error(e.getMessage());
        }
        return null;
    }

    public static Adresse toAdresse(String JsonAdresse){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(objectMapper.readTree(JsonAdresse));
            return objectMapper.readValue(jsonString, Adresse.class);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return null;
        }
    }
}


