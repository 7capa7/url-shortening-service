package pl.patryk.shortener.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.patryk.shortener.config.AppConfig;
import pl.patryk.shortener.entity.User;
import pl.patryk.shortener.exception.InvalidDataException;
import pl.patryk.shortener.exception.UserAlreadyExistsException;
import pl.patryk.shortener.exception.UserDoesNotExistOrPasswordIsInvalidException;
import pl.patryk.shortener.repository.UserRepository;
import pl.patryk.shortener.utils.AuthRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private AuthRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new AuthRequest("test@test.com", "password");
    }

    @Test
    void register_shouldThrowInvalidDataException_whenRequestIsInvalid() {
        AuthRequest invalidEmptyString = new AuthRequest("", "");
        AuthRequest invalidNulls = new AuthRequest(null, null);
        AuthRequest invalidWhiteSpaces = new AuthRequest("          ", "       ");

        assertThrows(InvalidDataException.class, () -> authenticationService.register(invalidEmptyString));
        assertThrows(InvalidDataException.class, () -> authenticationService.register(invalidNulls));
        assertThrows(InvalidDataException.class, () -> authenticationService.register(invalidWhiteSpaces));
    }

    @Test
    void register_shouldThrowUserAlreadyExistsException_whenEmailExists() {
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.register(validRequest));
    }

    @Test
    void register_shouldSaveUser_whenValidRequest() {
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validRequest.getPassword())).thenReturn("hash123");

        authenticationService.register(validRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(validRequest.getEmail(), userCaptor.getValue().getEmail());
        assertEquals("hash123", userCaptor.getValue().getPassword());
    }

    @Test
    void login_shouldThrow_whenRequestIsInvalid() {
        AuthRequest invalidNulls = new AuthRequest(null, null);
        AuthRequest invalidEmptyString = new AuthRequest("", "");
        AuthRequest invalidWhiteSpaces = new AuthRequest("  ", "     ");

        assertThrows(UserDoesNotExistOrPasswordIsInvalidException.class, () -> authenticationService.login(invalidNulls));
        assertThrows(UserDoesNotExistOrPasswordIsInvalidException.class, () -> authenticationService.login(invalidEmptyString));
        assertThrows(UserDoesNotExistOrPasswordIsInvalidException.class, () -> authenticationService.login(invalidWhiteSpaces));
    }

    @Test
    void login_shouldThrow_whenUserDoesNotExist() {
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistOrPasswordIsInvalidException.class, () -> authenticationService.login(validRequest));
    }

    @Test
    void login_shouldThrow_whenBadCredentials() {
        when(userRepository.findByEmail(validRequest.getEmail()))
                .thenReturn(Optional.of(User.builder().email(validRequest.getEmail()).password("hash123").build()));

        doThrow(BadCredentialsException.class).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(UserDoesNotExistOrPasswordIsInvalidException.class, () -> authenticationService.login(validRequest));
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreCorrect() {
        User user = User.builder().email(validRequest.getEmail()).password("hash123").build();

        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");

        String result = authenticationService.login(validRequest);

        assertEquals("token", result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
