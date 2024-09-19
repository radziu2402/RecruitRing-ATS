package pl.pwr.recruitringcore.service;

import pl.pwr.recruitringcore.dto.JwtResultDto;
import pl.pwr.recruitringcore.dto.LoginDTO;
import pl.pwr.recruitringcore.dto.ProfileDataDto;
import pl.pwr.recruitringcore.dto.UserDto;

public interface UserService {

    JwtResultDto login(LoginDTO credentialsDto);

    UserDto findUserByLogin(String login);

    ProfileDataDto getProfileData(UserDto userDto);

    ProfileDataDto updateProfileData(UserDto userDto, ProfileDataDto profileDataDto);
}
