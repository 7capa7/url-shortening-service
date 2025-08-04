package pl.patryk.shortener.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.patryk.shortener.entity.User;

@Component
public class BaseController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return ((User) auth.getPrincipal());
        }
        return null;
    }

    protected <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    protected <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(201).body(body);
    }
}
