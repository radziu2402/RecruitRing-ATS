package pl.pwr.recruitringcore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.AuthApi;
import pl.pwr.recruitringcore.dto.JwtResultDTO;
import pl.pwr.recruitringcore.dto.LoginDTO;
import pl.pwr.recruitringcore.dto.RegisterDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.exceptions.UserAlreadyExistsException;
import pl.pwr.recruitringcore.service.AuthService;
import pl.pwr.recruitringcore.service.UserServiceImpl;


@RestController
public class AuthController implements AuthApi {

    private final UserServiceImpl userService;
    private final AuthService authService;

    public AuthController(UserServiceImpl userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<Object> login(LoginDTO loginDTO) {
        JwtResultDTO jwtResult = userService.login(loginDTO);

        if (jwtResult.isSuccess()) {
            return new ResponseEntity<>(jwtResult, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<UserDTO> register(RegisterDTO registerDto) {
        UserDTO registeredUser = userService.register(registerDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> verifyEmail(String email) {
        authService.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> confirmVerificationCode(String email, String code) {
        boolean isVerified = authService.verifyCode(email, code);
        if (isVerified) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
