package pl.patryk.shortener.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.patryk.shortener.entity.ShortenedUrl;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortenedUrlDTO {
    private String originalUrl;
    private String shortenedUrl;
    private Long clicks;
    private LocalDateTime createdAt;

    public static ShortenedUrlDTO mapFrom(ShortenedUrl shortenedUrl, String baseUrl) {
        return ShortenedUrlDTO.builder()
                .originalUrl(shortenedUrl.getOriginalUrl())
                .shortenedUrl(baseUrl + "/" + shortenedUrl.getTag())
                .clicks(shortenedUrl.getClickCount())
                .createdAt(shortenedUrl.getCreatedAt())
                .build();
    }
}
