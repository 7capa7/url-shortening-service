package pl.patryk.shortener.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ShortenUrlRequestTest {

    @Test
    void validHttpUrl_shouldBeValid() {
        ShortenUrlRequest request = ShortenUrlRequest.builder()
                .url("https://google.com")
                .build();

        ShortenUrlRequest requestTag = ShortenUrlRequest.builder()
                .url("https://google.com")
                .customTag("custom123_t-a-g")
                .build();

        assertTrue(requestTag.isValid());
        assertTrue(request.isValid());
    }

    @Test
    void badUrl_shouldBeInvalid() {
        ShortenUrlRequest requestNoProtocol = ShortenUrlRequest.builder()
                .url("google.com")
                .build();

        ShortenUrlRequest requestUnsupported = ShortenUrlRequest.builder()
                .url("ftp://google.com")
                .build();

        ShortenUrlRequest requestBlank = ShortenUrlRequest.builder()
                .url("   ")
                .build();

        ShortenUrlRequest requestNull = ShortenUrlRequest.builder()
                .url(null)
                .build();

        ShortenUrlRequest requestEmpty = ShortenUrlRequest.builder()
                .url("")
                .build();

        assertFalse(requestEmpty.isValid());
        assertFalse(requestNull.isValid());
        assertFalse(requestBlank.isValid());
        assertFalse(requestUnsupported.isValid());
        assertFalse(requestNoProtocol.isValid());
    }

    @Test
    void BadCustomTag_shouldBeInvalid() {
        ShortenUrlRequest requestSpace = ShortenUrlRequest.builder()
                .url("https://google.com")
                .customTag("bad tag")
                .build();

        ShortenUrlRequest requestForbidenChar = ShortenUrlRequest.builder()
                .url("https://google.com")
                .customTag("bad/tag")
                .build();

        assertFalse(requestSpace.isValid());
        assertFalse(requestForbidenChar.isValid());
    }
}
