package pl.patryk.shortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.patryk.shortener.entity.ShortenedUrl;
import pl.patryk.shortener.entity.User;
import pl.patryk.shortener.exception.InvalidDataException;
import pl.patryk.shortener.exception.TagAlreadyExistsException;
import pl.patryk.shortener.repository.ShortenedUrlRepository;
import pl.patryk.shortener.utils.RandomTagGenerator;
import pl.patryk.shortener.utils.ShortenUrlRequest;
import pl.patryk.shortener.utils.ShortenedUrlDTO;

import java.time.LocalDateTime;
import java.util.List;

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

        ShortenedUrl shortUrl = ShortenedUrl.builder()
                .originalUrl(request.getUrl())
                .tag(tag)
                .owner(currentUser)
                .clickCount(0L)
                .createdAt(LocalDateTime.now())
                .build();

        shortedUrlRepository.save(shortUrl);

        return this.baseUrl + "/" + tag;
    }

    public String getOriginalUrl(String tag) {
        return shortedUrlRepository.findByTag(tag)
                .map(e -> {
                    e.setClickCount(e.getClickCount() + 1);
                    shortedUrlRepository.save(e);
                    return e.getOriginalUrl();
                }).orElseThrow(InvalidDataException::new);
    }

    public List<ShortenedUrlDTO> getUserUrls(User currentUser) {
        return shortedUrlRepository.findAllByOwner(currentUser)
                .stream().map(e -> ShortenedUrlDTO.mapFrom(e, baseUrl)).toList();
    }
}
