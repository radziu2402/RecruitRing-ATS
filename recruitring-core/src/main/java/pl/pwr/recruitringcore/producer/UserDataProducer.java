package pl.pwr.recruitringcore.producer;


import pl.pwr.recruitringcore.dto.ProfileDataDto;
import pl.pwr.recruitringcore.dto.UserDto;
import pl.pwr.recruitringcore.model.enums.Role;

public interface UserDataProducer {

    ProfileDataDto buildUserData(UserDto userDto);

    boolean supports(Role role);
}
