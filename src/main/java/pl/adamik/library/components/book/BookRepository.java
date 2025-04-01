package pl.adamik.library.components.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b where lower(b.title) like lower(concat('%', :search, '%'))" +
            "or lower(b.author) like lower(concat('%', :search, '%'))" +
            "or lower(b.isbn) like lower(concat('%', :search, '%'))")
    List<Book> findAllByTitleOrAuthorOrIsbn(@Param("search") String search);
}

