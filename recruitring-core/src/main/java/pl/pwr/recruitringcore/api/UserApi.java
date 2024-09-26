package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pwr.recruitringcore.dto.ProfileDataDRO;
import pl.pwr.recruitringcore.dto.UserDTO;

@RequestMapping("api/v1/")
public interface UserApi {

    @GetMapping("profile")
    ResponseEntity<Object> getProfileData(@AuthenticationPrincipal UserDTO userDto);

    @PostMapping("profile")
    ResponseEntity<Object> updateProfileData(@AuthenticationPrincipal UserDTO userDto, @RequestBody ProfileDataDRO profileDataDRO);
}
