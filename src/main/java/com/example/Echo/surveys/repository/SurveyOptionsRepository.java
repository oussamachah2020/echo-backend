package com.example.Echo.surveys.repository;

import com.example.Echo.surveys.models.SurveyOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyOptionsRepository extends JpaRepository<SurveyOptions, String> {
}
