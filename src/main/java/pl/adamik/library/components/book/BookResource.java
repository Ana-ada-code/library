package pl.adamik.library.components.book;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.adamik.library.components.book.dto.BookDetails;
import pl.adamik.library.components.book.dto.BookDto;
import pl.adamik.library.components.book.dto.BookLoanDto;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookResource {

    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    public List<BookDto> findAll(@RequestParam(required = false) String text) {
        if (text != null) {
            return bookService.findByTitleOrAuthorOrIsbn(text);
        } else {
            return bookService.findAll();
        }
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookDto> save(@RequestBody @Valid BookDto book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        }

        if (book.getId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The object being saved cannot have an id set"
            );
        }
        BookDto savedBook = bookService.save(book);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBook.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(@PathVariable Long id,
                                          @RequestBody BookDto book) {
        if (!id.equals(book.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The object being updated should have an ID matching the ID in the resource path"
            );
        }
        BookDto updatedBook = bookService.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/{id}/loans")
    public List<BookLoanDto> getAssetAssignments(@PathVariable Long id) {
        return bookService.getBookLoans(id);
    }

    @GetMapping("/details/{isbn}")
    public BookDetails getBookDetails(@PathVariable String isbn) {
        return bookService.getBookDetails(isbn);
    }

}
