package pl.patryk.shortener.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUrlRequest {
    private static final Pattern CUSTOM_TAG_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");

    private String url;
    private boolean priv;
    private String customTag;


    public boolean isValid() {
        if (url == null || url.isBlank()) {
            return false;
        }
        try {
            URL parsed = new URL(url);
            String protocol = parsed.getProtocol();
            if (!protocol.equals("http") && !protocol.equals("https")) return false;
        } catch (MalformedURLException e) {
            return false;
        }

        if (customTag != null && !customTag.isBlank()) {
            return CUSTOM_TAG_PATTERN.matcher(customTag).matches();
        }

        return true;
    }
}
