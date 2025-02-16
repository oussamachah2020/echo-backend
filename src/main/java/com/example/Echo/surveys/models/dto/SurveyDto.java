package com.example.Echo.surveys.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SurveyDto {
    public String id;
    public LocalDateTime createAt;

    public SurveyDto() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime updatedAt;
    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SurveyDto(String id, LocalDateTime createAt, LocalDateTime updatedAt, String title, String description, List<SurveyOptionDto> options) {
        this.id = id;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.description = description;
        this.options = options;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SurveyOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<SurveyOptionDto> options) {
        this.options = options;
    }

    public String description;
    public List<SurveyOptionDto> options;
}
