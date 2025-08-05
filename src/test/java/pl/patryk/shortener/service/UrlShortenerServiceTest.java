package pl.patryk.shortener.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.patryk.shortener.entity.ShortenedUrl;
import pl.patryk.shortener.entity.User;
import pl.patryk.shortener.exception.InvalidDataException;
import pl.patryk.shortener.exception.TagAlreadyExistsException;
import pl.patryk.shortener.repository.ShortenedUrlRepository;
import pl.patryk.shortener.utils.ShortenUrlRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerServiceTest {
    @Mock
    private ShortenedUrlRepository repository;
    @InjectMocks
    private UrlShortenerService service;

    @BeforeEach
    void setup() {
        try {
            var field = UrlShortenerService.class.getDeclaredField("baseUrl");
            field.setAccessible(true);
            field.set(service, "http://host:8080");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldShortenUrl_withCustomTag() {
        var request = ShortenUrlRequest.builder()
                .url("https://google.com")
                .customTag("tag123")
                .build();

        when(repository.existsByTag("tag123")).thenReturn(false);

        String result = service.shortenUrl(request, null);

        assertEquals("http://host:8080/tag123", result);

        ArgumentCaptor<ShortenedUrl> captor = ArgumentCaptor.forClass(ShortenedUrl.class);
        verify(repository).save(captor.capture());

        ShortenedUrl saved = captor.getValue();
        assertEquals("https://google.com", saved.getOriginalUrl());
        assertEquals("tag123", saved.getTag());
        assertNull(saved.getOwner());
    }

    @Test
    void shouldThrow_when_tagAlreadyExists() {
        var request = ShortenUrlRequest.builder()
                .url("https://google.com")
                .customTag("111")
                .build();

        when(repository.existsByTag("111")).thenReturn(true);

        assertThrows(TagAlreadyExistsException.class, () -> service.shortenUrl(request, null));
    }

    @Test
    void shouldGenerateTag_whenTagNotProvided() {
        var request = ShortenUrlRequest.builder()
                .url("https://google.com")
                .build();

        when(repository.existsByTag(anyString())).thenReturn(false);

        String result = service.shortenUrl(request, null);
        System.out.println(result);
        assertTrue(result.matches("http://host:8080/[a-zA-Z0-9]+"));

        verify(repository).save(any());
    }

    @Test
    void shouldThrow_whenUrlInvalid() {
        var requestSpace = ShortenUrlRequest.builder()
                .url("ta g")
                .build();

        var requestSlash = ShortenUrlRequest.builder()
                .url("ta/g")
                .build();

        assertThrows(InvalidDataException.class, () -> service.shortenUrl(requestSpace, null));
        assertThrows(InvalidDataException.class, () -> service.shortenUrl(requestSlash, null));
    }
}
