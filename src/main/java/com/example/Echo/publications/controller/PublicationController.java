package com.example.Echo.publications.controller;

import com.example.Echo.auth.services.JwtService;
import com.example.Echo.publications.models.Publication;
import com.example.Echo.publications.models.PublicationDto;
import com.example.Echo.publications.models.PublicationProjection;
import com.example.Echo.publications.services.PublicationService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/publication")
public class PublicationController {
    @Autowired
    public PublicationService publicationService;


    @PostMapping("/create")
    public ResponseEntity<String> createPublication(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PublicationDto publicationDto) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            publicationService.createPublication(publicationDto, token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Publication Created !");

        }else {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }
    }

    @GetMapping("/read")
    public ResponseEntity<?> getUserPublications(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            List<PublicationProjection> publications = publicationService.getUserPublications(token);
            return ResponseEntity.status(HttpStatus.OK).body(publications);

        }else {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<PublicationProjection>> getAllPublications() {
        List<PublicationProjection> publications = publicationService.getAll();

        return ResponseEntity.status(200).body(publications);
    }

    @DeleteMapping("/remove/{pubId}")
    public ResponseEntity<String> removePublication(@PathVariable String pubId) {
        String response = publicationService.deletePublication(pubId);

        if(response.equals("Required")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Publication ID is required");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Publication deleted successfully !");
    }

    @PutMapping("/edit/{pubId}")
    public ResponseEntity<String> editPublication(@PathVariable String pubId, @RequestBody PublicationDto req) {
        String response = publicationService.updatePublication(pubId, req);

        if(response.equals("NotFound")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Publication Not Found");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Publication updated successfully !");
    }
}
