package pl.pwr.recruitringcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.recruitringcore.api.UserApi;
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
            ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(updatedProfileData);
    }
}
