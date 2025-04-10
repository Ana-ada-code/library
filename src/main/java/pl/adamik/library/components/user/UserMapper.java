package pl.adamik.library.components.user;

import pl.adamik.library.components.user.dto.UserDto;

public class UserMapper {
    static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPesel()
        );
    }

    static User toEntity(UserDto user) {
        User entity = new User();
        entity.setId(user.getId());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPesel(user.getPesel());
        return entity;
    }
}
