package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.pwr.recruitringcore.dto.PasswordResetConfirmDTO;
import pl.pwr.recruitringcore.dto.PasswordResetRequestDTO;
import pl.pwr.recruitringcore.dto.ProfileDataDTO;
import pl.pwr.recruitringcore.dto.UserDTO;

@RequestMapping("api/v1/")
public interface UserApi {

    @GetMapping("profile")
    ResponseEntity<Object> getProfileData(@AuthenticationPrincipal UserDTO userDto);

    @PostMapping("profile")
    ResponseEntity<Object> updateProfileData(@AuthenticationPrincipal UserDTO userDto, @RequestBody ProfileDataDTO profileDataDTO);

    @PostMapping("reset-password")
    ResponseEntity<Object> resetPassword(@RequestBody PasswordResetRequestDTO request);

    @PostMapping("reset-password/confirm")
    ResponseEntity<Object> confirmResetPassword(@RequestBody PasswordResetConfirmDTO confirmDTO);

    @GetMapping("reset-password/verify-token")
    ResponseEntity<Boolean> verifyResetToken(@RequestParam String token);

}
