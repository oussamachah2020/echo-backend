package com.example.Echo.publications.services;

import com.example.Echo.auth.models.User;
import com.example.Echo.auth.repositories.UserRepository;
import com.example.Echo.auth.services.JwtService;
import com.example.Echo.publications.models.Publication;
import com.example.Echo.publications.models.PublicationDto;
import com.example.Echo.publications.models.PublicationProjection;
import com.example.Echo.publications.repositories.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PublicationService {
    @Autowired
    public PublicationRepository repository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public JwtService jwtService;

    public void createPublication(PublicationDto publicationDto, String token) {
        String username = jwtService.extractUsername(token);
        User existingUser = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with this username exists"));

        Publication publication = new Publication();

        publication.setTitle(publicationDto.getTitle());
        publication.setDescription(publicationDto.getDescription());
        publication.setPhotoUrl(publicationDto.getPhotoUrl());
        publication.setUser(existingUser);

        repository.save(publication);
    }

    public List<PublicationProjection> getUserPublications(String token) {
        String username = jwtService.extractUsername(token);
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with this username exists"));

        return repository.findByUserId(existingUser.getId());
    }

    public List<PublicationProjection> getAll() {
        return repository.findAllPublications();
    }

    public String deletePublication(String pubId) {
        if(pubId == null) {
            return "Required";
        }

        repository.deleteById(pubId);
        return "DeleteSuccess";
    }

    public String updatePublication(String pubId, PublicationDto req) {
        if (pubId == null || req == null) {
            return "Required";
        }

        Optional<Publication> existingPublication = repository.findById(pubId);

        if (existingPublication.isPresent()) {
            Publication publication = existingPublication.get(); // Retrieve the entity

            // Update fields
            if (req.getTitle() != null) {
                publication.setTitle(req.getTitle());
            }
            if (req.getDescription() != null) {
                publication.setDescription(req.getDescription());
            }
            if (req.getPhotoUrl() != null) {
                publication.setPhotoUrl(req.getPhotoUrl());
            }

            publication.setUpdatedAt(LocalDateTime.now()); // Update timestamp if needed

            repository.save(publication); // Save the updated entity

            return "UpdateSuccess";
        } else {
            return "NotFound";
        }
    }

}
