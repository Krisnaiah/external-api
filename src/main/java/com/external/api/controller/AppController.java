

package com.external.api.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;

import com.external.api.dto.PrevalenceReqDTO;
import com.external.api.dto.PrevalenceRespDTO;

@RestController
public class AppController {

    @Autowired 
    RestTemplate restTemplate;

    @GetMapping("/status")
    public String status() {
        return "Server is up";
    }

    @GetMapping("/run")
    public String run() {
        
        ClassPathResource resource = new ClassPathResource(
            "com/external/api/controller/share-of-the-population-infected-with-hiv.csv");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                return "No Data";
            }

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] columns = line.split(",", -1);
                if (columns.length != 4) {
                    throw new IOException("Invalid CSV row: " + line);
                }

                PrevalenceReqDTO request = new PrevalenceReqDTO();
                request.setCountry(columns[0]);
                request.setCode(columns[1]);
                request.setYear(Integer.parseInt(columns[2]));
                request.setPrevalence(Double.parseDouble(columns[3]));
                
                ResponseEntity<PrevalenceRespDTO> response = restTemplate.postForEntity(
                    "http://localhost:8080/add", request, PrevalenceRespDTO.class);

                System.out.println("Done with " + String.join(", ", columns));
            }
        } catch (IOException | NumberFormatException exception) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Unable to read prevalence CSV", exception);
        }

        return "Finished populating DB";
        
    }
}
