package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pwr.recruitringcore.dto.LoginDTO;


@RequestMapping("api/v1/")
public interface AuthApi {

    @PostMapping("login")
    ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO);
}
