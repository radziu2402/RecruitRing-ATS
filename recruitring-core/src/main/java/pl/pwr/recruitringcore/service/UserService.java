package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.*;

public interface UserService {

    JwtResultDto login(LoginDTO credentialsDto);

    UserDto register(RegisterDTO registerDto);

    UserDto findUserByLogin(String login);

    UserDto findUserById(Integer id);

    ProfileDataDto getProfileData(UserDto userDto);

    ProfileDataDto updateProfileData(UserDto userDto, ProfileDataDto profileDataDto);
}
