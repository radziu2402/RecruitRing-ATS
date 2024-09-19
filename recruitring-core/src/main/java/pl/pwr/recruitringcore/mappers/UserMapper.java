package pl.pwr.recruitringcore.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.pwr.recruitringcore.dto.SignUpDto;
import pl.pwr.recruitringcore.dto.UserDto;
import pl.pwr.recruitringcore.model.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User signUpToUser(SignUpDto userDto);

    // Dodaj mapowanie ról ręcznie
    default String mapRoles(User user) {
        return user.getRole().name();
    }

    @Mapping(target = "role", expression = "java(mapRoles(user))") // Użyj ręcznie dodanej logiki
    UserDto toUserDtoWithRoles(User user);
}