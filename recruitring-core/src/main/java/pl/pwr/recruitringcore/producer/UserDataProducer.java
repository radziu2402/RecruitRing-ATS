package pl.pwr.recruitringcore.producer;


import pl.pwr.recruitringcore.dto.ProfileDataDTO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.enums.Role;

public interface UserDataProducer {

    ProfileDataDTO buildUserData(UserDTO userDto);

    boolean supports(Role role);
}
