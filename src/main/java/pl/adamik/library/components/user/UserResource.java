package pl.adamik.library.components.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.adamik.library.components.user.dto.UserDto;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    private final UserService userService;

    UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<UserDto> findAll(@RequestParam(required = false) String lastName) {
        if (lastName != null) {
            return userService.findByLastName(lastName);
        } else {
            return userService.findAll();
        }
    }

    @PostMapping("")
    public ResponseEntity<UserDto> save(@RequestBody UserDto user) {
        if (user.getId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Zapisywany obiekt nie może mieć ustawionego id"
            );
        }
        UserDto savedUser = userService.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedUser);
    }
}
