package pl.pwr.recruitringcore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.UserApi;
import pl.pwr.recruitringcore.dto.PasswordResetConfirmDTO;
import pl.pwr.recruitringcore.dto.PasswordResetRequestDTO;
import pl.pwr.recruitringcore.dto.ProfileDataDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.service.UserService;

@RestController
public class UserController implements UserApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Object> getProfileData(UserDTO userDto) {
        ProfileDataDTO profileData = userService.getProfileData(userDto);
        if (!profileData.isSuccess()) {
            ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(profileData);
    }

    @Override
    public ResponseEntity<Object> updateProfileData(UserDTO userDto, ProfileDataDTO profileDataDTO) {
        ProfileDataDTO updatedProfileData = userService.updateProfileData(userDto, profileDataDTO);
        if (!updatedProfileData.isSuccess()) {
            return ResponseEntity.badRequest().body("Niepoprawne obecne haslo lub blad podczas aktualizacji profilu");
        }
        return ResponseEntity.ok(updatedProfileData);
    }

    @Override
    public ResponseEntity<Object> resetPassword(PasswordResetRequestDTO request) {
        userService.sendPasswordResetLink(request.getLogin());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Object> confirmResetPassword(PasswordResetConfirmDTO confirmDTO) {
        boolean result = userService.resetPassword(confirmDTO.getToken(), confirmDTO.getNewPassword());
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<Boolean> verifyResetToken(String token) {
        boolean isValid = userService.isResetTokenValid(token);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        return ResponseEntity.ok(true);
    }

}
