package pl.patryk.shortener.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.patryk.shortener.entity.ShortenedUrl;
import pl.patryk.shortener.entity.User;
import pl.patryk.shortener.exception.InvalidDataException;
import pl.patryk.shortener.exception.TagAlreadyExistsException;
import pl.patryk.shortener.repository.ShortenedUrlRepository;
import pl.patryk.shortener.utils.RandomTagGenerator;
import pl.patryk.shortener.utils.ShortenUrlRequest;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    @Value("${app.host.url}")
    private String baseUrl;
    private final ShortenedUrlRepository shortedUrlRepository;

    public String shortenUrl(ShortenUrlRequest request, User currentUser) {
        if (!request.isValid()) throw new InvalidDataException();
        String tag;
        if (request.getCustomTag() != null && !request.getCustomTag().isBlank()) {
            if (shortedUrlRepository.existsByTag(request.getCustomTag())) {
                throw new TagAlreadyExistsException();
            }
            tag = request.getCustomTag();
        } else {
            do {
                tag = RandomTagGenerator.generate();
            } while (shortedUrlRepository.existsByTag(tag));
        }

        boolean isPrivate = currentUser != null && request.isPriv();

        ShortenedUrl shortUrl = ShortenedUrl.builder()
                .originalUrl(request.getUrl())
                .tag(tag)
                .owner(currentUser)
                .priv(isPrivate)
                .clickCount(0L)
                .createdAt(LocalDateTime.now())
                .build();

        shortedUrlRepository.save(shortUrl);

        return this.baseUrl + "/" + tag;
    }
}
