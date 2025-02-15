package com.example.Echo.publications.repositories;

import com.example.Echo.publications.models.Publication;
import com.example.Echo.publications.models.PublicationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, String> {
    List<PublicationProjection> findByUserId(String userId);

    @Query(value = "SELECT p.id, p.title, p.description, p.created_at, p.updated_at FROM Publication p", nativeQuery = true)
    List<PublicationProjection> findAllPublications();
}
