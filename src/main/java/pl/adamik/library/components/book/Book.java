package pl.adamik.library.components.book;

import jakarta.persistence.*;
import lombok.*;
import pl.adamik.library.components.genre.Genre;
import pl.adamik.library.components.loan.Loan;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    private Genre genre;
    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<Loan> loans = new ArrayList<>();
}
