package pl.patryk.shortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.patryk.shortener.entity.User;

@RestController
@RequestMapping("/api/test")
public class TestController extends BaseController {
    @GetMapping
    public ResponseEntity<String> hello() {
        User user = getCurrentUser();
        return ok("Hello World " + user.getEmail() + ".");
    }
}
