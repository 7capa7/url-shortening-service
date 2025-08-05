package pl.patryk.shortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.patryk.shortener.service.UrlShortenerService;
import pl.patryk.shortener.utils.ShortenResponse;
import pl.patryk.shortener.utils.ShortenUrlRequest;

@RestController
@RequestMapping("/api/shorten")
@RequiredArgsConstructor
public class UrlShortenerController extends BaseController {
    private final UrlShortenerService urlService;

    @PostMapping("/save")
    public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody ShortenUrlRequest request) {
        String shortened = urlService.shortenUrl(request, getCurrentUser());
        return ok(new ShortenResponse(200, shortened));
    }
}
