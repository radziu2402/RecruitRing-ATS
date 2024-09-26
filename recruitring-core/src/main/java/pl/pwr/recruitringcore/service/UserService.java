package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.*;

public interface UserService {

    JwtResultDTO login(LoginDTO credentialsDto);

    UserDTO register(RegisterDTO registerDto);

    UserDTO findUserByLogin(String login);

    UserDTO findUserById(Integer id);

    ProfileDataDRO getProfileData(UserDTO userDto);

    ProfileDataDRO updateProfileData(UserDTO userDto, ProfileDataDRO profileDataDRO);
}
