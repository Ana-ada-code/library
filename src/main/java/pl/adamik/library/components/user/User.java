package pl.adamik.library.components.user;

import jakarta.persistence.*;
import lombok.*;
import pl.adamik.library.components.loan.Loan;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String pesel;
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Loan> loans = new ArrayList<>();
}
