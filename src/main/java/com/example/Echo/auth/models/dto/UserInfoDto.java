package com.example.Echo.auth.models.dto;

public class UserInfoDto {
    public String id;
    public String username;
    public String email;
    public String photoUrl;

    public UserInfoDto(String id, String username, String email, Boolean verified, String photoUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.verified = verified;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean verified;
}
