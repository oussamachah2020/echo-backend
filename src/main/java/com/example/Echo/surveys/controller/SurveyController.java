package com.example.Echo.surveys.controller;

import com.example.Echo.surveys.models.Survey;
import com.example.Echo.surveys.models.dto.SurveyDto;
import com.example.Echo.surveys.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {
    @Autowired
    public SurveyService surveyService;

    @PostMapping("/create")
    public ResponseEntity<String> addSurvey(@RequestBody SurveyDto body, @RequestHeader("Authorization") String authorizationHeader) {
        if(body == null) {
            return ResponseEntity.status(400).body("Survey data is required");
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String response = surveyService.createSurvey(body, token);
            return ResponseEntity.status(200).body("Survey Created Successfully !");
        }

        return ResponseEntity.status(200).body("Invalid Authorization Token");
    }

    @GetMapping("/")
    public ResponseEntity<List<SurveyDto>> getAll() {
        List<SurveyDto> surveys = surveyService.getSurveys();

        return ResponseEntity.status(200).body(surveys);
    }
}
