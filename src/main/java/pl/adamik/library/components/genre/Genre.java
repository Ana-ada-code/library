package pl.adamik.library.components.genre;

import jakarta.persistence.*;
import lombok.*;
import pl.adamik.library.components.book.Book;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    @OneToMany(mappedBy = "genre")
    private Set<Book> books = new HashSet<>();
}
