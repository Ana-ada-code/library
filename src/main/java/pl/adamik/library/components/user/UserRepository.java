package pl.adamik.library.components.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByLastNameContainingIgnoreCase(String lastName);
}