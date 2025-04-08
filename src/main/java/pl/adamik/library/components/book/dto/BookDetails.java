package pl.adamik.library.components.book.dto;

import java.util.List;

public record BookDetails(
        int numberOfPages,
        String coverImage,
        String publishDate,
        List<String> publishers
) {
}
