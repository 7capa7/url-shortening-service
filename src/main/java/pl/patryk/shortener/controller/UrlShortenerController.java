package pl.patryk.shortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.patryk.shortener.service.UrlShortenerService;
import pl.patryk.shortener.utils.ShortenResponse;
import pl.patryk.shortener.utils.ShortenUrlRequest;
import pl.patryk.shortener.utils.ShortenedUrlDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController extends BaseController {
    private final UrlShortenerService urlService;

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody ShortenUrlRequest request) {
        String shortened = urlService.shortenUrl(request, getCurrentUser());
        return ok(new ShortenResponse(200, shortened));
    }

    @GetMapping("/{tag}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String tag) {
        return found(urlService.getOriginalUrl(tag));
    }

    @GetMapping("/api/my-links")
    public ResponseEntity<List<ShortenedUrlDTO>> getUserUrls() {
        return ok(urlService.getUserUrls(getCurrentUser()));
    }

}
