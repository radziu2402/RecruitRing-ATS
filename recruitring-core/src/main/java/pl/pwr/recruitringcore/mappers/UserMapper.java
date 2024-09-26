package pl.pwr.recruitringcore.mappers;


import org.mapstruct.Mapper;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.model.entities.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDto(User user);
}