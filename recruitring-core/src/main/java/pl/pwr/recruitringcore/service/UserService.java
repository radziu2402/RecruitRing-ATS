package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.*;

public interface UserService {

    JwtResultDTO login(LoginDTO credentialsDto);

    UserDTO register(RegisterDTO registerDto);

    UserDTO findUserByLogin(String login);

    UserDTO findUserById(Long id);

    ProfileDataDTO getProfileData(UserDTO userDto);

    ProfileDataDTO updateProfileData(UserDTO userDto, ProfileDataDTO profileDataDTO);
}
