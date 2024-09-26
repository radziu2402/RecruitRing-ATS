package pl.pwr.recruitringcore.producer;


import pl.pwr.recruitringcore.dto.ProfileDataDRO;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.enums.Role;

public interface UserDataProducer {

    ProfileDataDRO buildUserData(UserDTO userDto);

    boolean supports(Role role);
}
