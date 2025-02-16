package com.example.Echo.surveys.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "survey_options")
public class SurveyOptions {
    @Id
    @Column(name = "id")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Column(name = "text")
    String text;

    @ManyToOne()
    @JoinColumn(name = "survey_id")
    Survey survey;
}
