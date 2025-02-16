package com.example.Echo.surveys.services;

import com.example.Echo.auth.models.User;
import com.example.Echo.auth.repositories.UserRepository;
import com.example.Echo.auth.services.JwtService;
import com.example.Echo.surveys.models.Survey;
import com.example.Echo.surveys.models.SurveyOptions;
import com.example.Echo.surveys.models.dto.SurveyDto;
import com.example.Echo.surveys.models.dto.SurveyOptionDto;
import com.example.Echo.surveys.repository.SurveyOptionsRepository;
import com.example.Echo.surveys.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    @Autowired
    public SurveyRepository repository;

    @Autowired
    public SurveyOptionsRepository surveyOptionsRepository;


    @Autowired
    public JwtService jwtService;

    @Autowired
    public UserRepository userRepository;


    @Transactional
    public String createSurvey(SurveyDto body, String token) {
        if (body == null) {
            return "BodyRequired";
        }

        String username = jwtService.extractUsername(token);
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with this username exists"));

        // Create and save the survey
        Survey survey = new Survey();
        survey.setTitle(body.getTitle());
        survey.setDescription(body.getDescription());
        survey.setUser(existingUser);
        survey = repository.save(survey);

        // Save survey options
        Survey finalSurvey = survey;
        List<SurveyOptions> options = body.getOptions().stream().map(optionDto -> {
            SurveyOptions surveyOptions = new SurveyOptions();
            surveyOptions.setId(UUID.randomUUID().toString());
            surveyOptions.setText(optionDto.getText());
            surveyOptions.setSurvey(finalSurvey);
            return surveyOptions;
        }).collect(Collectors.toList());

        surveyOptionsRepository.saveAll(options);

        return "SurveySuccess";
    }

    public List<SurveyDto> getSurveys() {
        List<Survey> surveys = repository.findAll();

        return surveys.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private SurveyDto convertToDto(Survey survey) {
        SurveyDto dto = new SurveyDto();
        dto.setId(survey.getId());
        dto.setTitle(survey.getTitle());
        dto.setDescription(survey.getDescription());

        dto.setCreateAt(survey.getCreatedAt());
        dto.setUpdatedAt(survey.getUpdatedAt());

        // Convert survey options to SurveyOptionDto
        List<SurveyOptionDto> optionDtos = survey.getOptions().stream().map(option -> {
            SurveyOptionDto optionDto = new SurveyOptionDto();
            optionDto.setId(option.getId());
            optionDto.setText(option.getText());
            return optionDto;
        }).collect(Collectors.toList());

        dto.setOptions(optionDtos);
        return dto;
    }

}
