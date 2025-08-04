package pl.patryk.shortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.patryk.shortener.service.AuthenticationService;
import pl.patryk.shortener.utils.AuthRequest;
import pl.patryk.shortener.utils.BaseResponse;
import pl.patryk.shortener.utils.JwtResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody AuthRequest request) {
        return ok(new JwtResponse(200, authenticationService.login(request)));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> register(@RequestBody AuthRequest request) {
        authenticationService.register(request);
        return created(new BaseResponse(201, "Created"));
    }
}