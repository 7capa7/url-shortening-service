package pl.patryk.shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.patryk.shortener.entity.ShortenedUrl;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, String> {
    boolean existsByTag(String tag);
}
