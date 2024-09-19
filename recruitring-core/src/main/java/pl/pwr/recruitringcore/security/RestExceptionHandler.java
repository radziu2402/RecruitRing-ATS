package pl.pwr.recruitringcore.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.pwr.recruitringcore.dto.ErrorDto;
import pl.pwr.recruitringcore.exceptions.UnknownUserException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {UnknownUserException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(UnknownUserException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorDto(ex.getMessage()));
    }
}