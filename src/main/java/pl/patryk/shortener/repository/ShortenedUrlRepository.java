package pl.patryk.shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.patryk.shortener.entity.ShortenedUrl;
import pl.patryk.shortener.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, String> {
    boolean existsByTag(String tag);

    Optional<ShortenedUrl> findByTag(String tag);

    List<ShortenedUrl> findAllByOwner(User owner);
}
