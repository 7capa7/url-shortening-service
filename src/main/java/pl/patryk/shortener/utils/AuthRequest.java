package pl.patryk.shortener.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email;
    private String password;

    public boolean isValid() {
        if (email == null || password == null) return false;
        return !email.isBlank() && !password.isBlank();
    }
}