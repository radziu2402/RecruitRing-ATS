package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.JwtResultDTO;
import pl.pwr.recruitringcore.dto.LoginDTO;
import pl.pwr.recruitringcore.dto.ProfileDataDTO;
import pl.pwr.recruitringcore.dto.UserDTO;

public interface UserService {

    JwtResultDTO login(LoginDTO credentialsDto);

    UserDTO findUserByLogin(String login);

    ProfileDataDTO getProfileData(UserDTO userDto);

    ProfileDataDTO updateProfileData(UserDTO userDto, ProfileDataDTO profileDataDTO);

    void sendPasswordResetLink(String login);

    boolean resetPassword(String token, String newPassword);

    boolean isResetTokenValid(String token);

}
