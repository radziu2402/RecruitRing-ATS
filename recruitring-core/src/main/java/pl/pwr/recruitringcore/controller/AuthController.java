package pl.pwr.recruitringcore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.AuthApi;
import pl.pwr.recruitringcore.dto.JwtResultDto;
import pl.pwr.recruitringcore.dto.LoginDTO;
import pl.pwr.recruitringcore.dto.RegisterDTO;
import pl.pwr.recruitringcore.dto.UserDto;
import pl.pwr.recruitringcore.exceptions.UserAlreadyExistsException;
import pl.pwr.recruitringcore.service.UserServiceImpl;


@RestController
public class AuthController implements AuthApi {

    private final UserServiceImpl userService;

    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Object> login(LoginDTO loginDTO) {
        JwtResultDto jwtResult = userService.login(loginDTO);

        if (jwtResult.isSuccess()) {
            return new ResponseEntity<>(jwtResult, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<UserDto> register(RegisterDTO registerDto) {
        UserDto registeredUser = userService.register(registerDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
