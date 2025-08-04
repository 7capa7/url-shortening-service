package pl.patryk.shortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.patryk.shortener.entity.User;
import pl.patryk.shortener.exception.InvalidDataException;
import pl.patryk.shortener.exception.UserAlreadyExistsException;
import pl.patryk.shortener.exception.UserDoesNotExistOrPasswordIsInvalidException;
import pl.patryk.shortener.repository.UserRepository;
import pl.patryk.shortener.utils.AuthRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(AuthRequest request) {
        if (!request.isValid()) throw new InvalidDataException();
        if (checkIfUserExist(request.getEmail())) throw new UserAlreadyExistsException();

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    public String login(AuthRequest request) {

        if (!request.isValid()) throw new UserDoesNotExistOrPasswordIsInvalidException();

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserDoesNotExistOrPasswordIsInvalidException::new);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException exception) {
            throw new UserDoesNotExistOrPasswordIsInvalidException();
        }

        return jwtService.generateToken(user);
    }

    private boolean checkIfUserExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }
}
