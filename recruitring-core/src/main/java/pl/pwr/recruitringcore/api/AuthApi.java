package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pwr.recruitringcore.dto.LoginDTO;


@RequestMapping("api/v1/")
public interface AuthApi {

    @PostMapping("login")
    ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO);

    @PostMapping("verify-email")
    ResponseEntity<String> verifyEmail(@RequestParam("email") String email);

    @PostMapping("confirm-code")
    ResponseEntity<String> confirmVerificationCode(@RequestParam("email") String email, @RequestParam("code") String code);
}
