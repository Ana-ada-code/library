package pl.adamik.library.components.user;

import pl.adamik.library.components.user.dto.UserDto;

class UserMapper {
    static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPesel()
        );
    }

    static User toEntity(UserDto user) {
        return User.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .pesel(user.getPesel())
                .build();
    }
}
