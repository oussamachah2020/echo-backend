package com.example.Echo.publications.models;

import java.time.LocalDateTime;

public interface PublicationProjection {
    String getId();
    String getTitle();
    String getDescription();
    String getPhotoUrl();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
